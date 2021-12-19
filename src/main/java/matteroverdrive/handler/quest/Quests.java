
package matteroverdrive.handler.quest;

import matteroverdrive.api.quest.IQuest;
import matteroverdrive.api.quest.Quest;

import java.util.*;

public class Quests {
    private static final int MIN_QUEST_BIT = 0;
    private static final int MAX_QUEST_IDS = Short.MAX_VALUE;
    public final Random random;
    private final BitSet bitSet;
    private final Map<Integer, IQuest> questMap;
    private final Map<IQuest, Integer> questIntegerMap;
    private final Map<String, IQuest> stringQuestMap;

    public Quests() {
        bitSet = new BitSet(MAX_QUEST_IDS);
        questMap = new HashMap<>();
        questIntegerMap = new HashMap<>();
        stringQuestMap = new HashMap<>();
        random = new Random();
    }

    public IQuest getQuestWithID(int questID) {
        return questMap.get(questID);
    }

    public int getQuestID(IQuest quest) {
        return questIntegerMap.get(quest);
    }

    public IQuest getQuestByName(String name) {
        return stringQuestMap.get(name);
    }

    public void registerQuest(String name, Quest quest) {
        if (questIntegerMap.containsKey(quest)) {
            throw new RuntimeException(name + " Quest is already registered");
        }
        int id = bitSet.nextClearBit(MIN_QUEST_BIT);
        questMap.put(id, quest);
        questIntegerMap.put(quest, id);
        stringQuestMap.put(name, quest);
        bitSet.set(id, true);
    }

    public void registerQuestAt(Integer id, String name, IQuest quest) {
        questMap.put(id, quest);
        questIntegerMap.put(quest, id);
        bitSet.set(id, true);
        stringQuestMap.put(name, quest);
    }

    public Set<String> getAllQuestName() {
        return stringQuestMap.keySet();
    }
}
