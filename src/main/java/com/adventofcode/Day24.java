package com.adventofcode;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.vavr.collection.Stream.iterate;
import static java.util.Comparator.comparingInt;

class Day24 {

    private static final Pattern GROUP_PATTERN = Pattern.compile(
            "(?<units>\\d+) units each with (?<hp>\\d+) hit points.*? with " +
                    "an attack that does (?<attack>\\d+) (?<attackType>\\w+) damage at initiative (?<initiative>\\d+)");
    private static final Pattern WEAKNESSES_PATTERN = Pattern.compile("weak to (.+?)[;)]");
    private static final Pattern IMMUNITIES_PATTERN = Pattern.compile("immune to (.+?)[;)]");

    int solvePart1(List<String> input) {
        List<Group> groups = parseGroups(input);
        List<Group> outcome = performCombat(groups);

        return getTotalUnits(outcome);
    }

    int solvePart2(int startBoost, List<String> input) {
        List<Group> groups = parseGroups(input);

        return iterate(startBoost, i -> i + 1)
                .map(boost -> boostImmuneSystemAttack(groups, boost))
                .map(this::performCombat)
                .dropUntil(this::immuneSystemWins)
                .map(this::getTotalUnits)
                .get();
    }

    private List<Group> boostImmuneSystemAttack(List<Group> groups, int boost) {
        return groups.map(group -> group.groupType == GroupType.IMMUNE_SYSTEM ? group.boostAttack(boost) : group);
    }

    private List<Group> performCombat(List<Group> groups) {
        return iterate(groups, this::fight)
                .dropUntil(this::combatFinished)
                .head();
    }

    private List<Group> fight(List<Group> groups) {
        Map<Integer, Group> selectedTargets = selectTargets(groups);

        return attack(groups, selectedTargets);
    }

    private Map<Integer, Group> selectTargets(List<Group> groups) {
        List<Group> ordered = groups.sorted(comparingInt(Group::getEffectivePower)
                .thenComparingInt(Group::getInitiative).reversed());

        Map<GroupType, List<Group>> byGroupType = ordered.groupBy(Group::getGroupType);
        java.util.Map<Integer, Group> selectedTargets = new java.util.HashMap<>();
        Set<Integer> taken = new HashSet<>();

        ordered.forEach(group -> {
            List<Group> enemies = getEnemies(group, byGroupType);
            selectTarget(group, enemies, g -> taken.contains(g.id)).forEach(target -> {
                        selectedTargets.put(group.id, target);
                        taken.add(target.id);
                    });
                });

        return HashMap.ofAll(selectedTargets);
    }

    private List<Group> getEnemies(Group group, Map<GroupType, List<Group>> groups) {
        return groups.filterKeys(t -> t != group.groupType).values().flatMap(l -> l).toList();
    }

    private Option<Group> selectTarget(Group group, List<Group> enemies, Predicate<Group> isSelected) {
        return enemies.filter(target -> !isSelected.test(target))
                .map(target -> Tuple.of(target, getDealtDamage(group, target)))
                .filter(t -> t._2 > 0)
                .maxBy(Comparator.<Tuple2<Group, Integer>>comparingInt(Tuple2::_2)
                        .thenComparingInt(t -> t._1.getEffectivePower())
                        .thenComparingInt(t -> t._1.initiative)
                )
                .map(Tuple2::_1);
    }

    private List<Group> attack(List<Group> groups, Map<Integer, Group> selectedTargets) {
        List<Group> ordered = groups.sorted(comparingInt(Group::getInitiative).reversed());

        java.util.Map<Integer, Group> attacked = new java.util.HashMap<>();

        for (Group group : ordered) {
            Group attacker = attacked.getOrDefault(group.id, group);
            if (attacker.units > 0) {
                selectedTargets.get(attacker.id).map(target ->
                        attack(attacker, target)).forEach(a -> attacked.put(a.id, a)
                );
            }
        }

        return groups.map(g -> attacked.getOrDefault(g.id, g)).filter(g -> g.units > 0);
    }

    private Group attack(Group attacker, Group attacked) {
        int killed = getDealtDamage(attacker, attacked) / attacked.hitPoints;

        return attacked.reduceUnitsBy(killed);
    }

    private int getDealtDamage(Group attacker, Group attacked) {
        if (attacked.immunities.contains(attacker.attackType)) {
            return 0;
        }

        int effectivePower = attacker.getEffectivePower();

        if (attacked.weaknesses.contains(attacker.attackType)) {
            return effectivePower * 2;
        }

        return effectivePower;
    }

    private boolean combatFinished(List<Group> groups) {
        return groups.map(Group::getGroupType).toSet().length() <= 1;
    }

    private boolean immuneSystemWins(List<Group> outcome) {
        return outcome.forAll(g -> g.groupType == GroupType.IMMUNE_SYSTEM);
    }

    private int getTotalUnits(List<Group> groups) {
        return groups.map(g -> g.units).sum().intValue();
    }

    private List<Group> parseGroups(List<String> input) {
        Tuple2<List<String>, List<String>> split = input.splitAt(""::equals);

        List<String> immune = split._1.tail();
        List<String> infection = split._2.drop(2);

        return parse(GroupType.IMMUNE_SYSTEM, immune, 0)
                .appendAll(parse(GroupType.INFECTION, infection, immune.length()));
    }

    private List<Group> parse(GroupType type, List<String> groups, int idStart) {
        return groups.zipWithIndex().map(t -> parse(type, idStart + t._2, t._1, t._2 + 1));
    }

    private Group parse(GroupType type, Integer id, String group, Integer groupId) {
        Matcher matcher = GROUP_PATTERN.matcher(group);

        if (matcher.find()) {
            int units = Integer.parseInt(matcher.group("units"));
            int hitPoints = Integer.parseInt(matcher.group("hp"));
            int attackPower = Integer.parseInt(matcher.group("attack"));
            int initiative = Integer.parseInt(matcher.group("initiative"));
            String attackType = matcher.group("attackType");
            List<String> weaknesses = parseWeaknesses(group);
            List<String> immunities = parseImmunities(group);

            return new Group(
                    id,
                    groupId,
                    type,
                    units,
                    hitPoints,
                    attackPower,
                    initiative,
                    attackType,
                    weaknesses,
                    immunities
            );
        }

        throw new IllegalArgumentException(group);
    }

    private List<String> parseWeaknesses(String group) {
        List<String> weaknesses = List.of();
        Matcher wMatcher = WEAKNESSES_PATTERN.matcher(group);
        if (wMatcher.find()) {
            weaknesses = List.of(wMatcher.group(1).split(", "));
        }
        return weaknesses;
    }

    private List<String> parseImmunities(String group) {
        List<String> immunities = List.of();
        Matcher iMatcher = IMMUNITIES_PATTERN.matcher(group);
        if (iMatcher.find()) {
            immunities = List.of(iMatcher.group(1).split(", "));
        }
        return immunities;
    }

    private static class Group {
        final Integer id;
        final Integer groupId;
        final GroupType groupType;
        final int units;
        final int hitPoints;
        final int attackDamage;
        final int initiative;
        final String attackType;
        final List<String> weaknesses;
        final List<String> immunities;

        Group(Integer id, Integer groupId, GroupType groupType, int units, int hitPoints, int attackDamage,
              int initiative, String attackType, List<String> weaknesses, List<String> immunities) {
            this.id = id;
            this.groupId = groupId;
            this.groupType = groupType;
            this.units = units;
            this.hitPoints = hitPoints;
            this.attackDamage = attackDamage;
            this.initiative = initiative;
            this.attackType = attackType;
            this.weaknesses = weaknesses;
            this.immunities = immunities;
        }

        int getEffectivePower() {
            return units * attackDamage;
        }

        GroupType getGroupType() {
            return groupType;
        }

        int getInitiative() {
            return initiative;
        }

        Group boostAttack(int boost) {
            return new Group(
                    id,
                    groupId,
                    groupType,
                    units,
                    hitPoints,
                    boost + attackDamage,
                    initiative,
                    attackType,
                    weaknesses,
                    immunities
            );
        }

        Group reduceUnitsBy(int units) {
            return new Group(
                    id,
                    groupId,
                    groupType,
                    this.units - Math.min(this.units, units),
                    hitPoints,
                    attackDamage,
                    initiative,
                    attackType,
                    weaknesses,
                    immunities
            );
        }
    }

    private enum GroupType {
        IMMUNE_SYSTEM, INFECTION
    }
}
