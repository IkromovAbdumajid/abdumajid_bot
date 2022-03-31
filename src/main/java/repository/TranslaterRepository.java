package repository;

import controller.Translater;
import dto.TodoItem;
import dto.TranslaterItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TranslaterRepository {
    private Map<Long, List<TranslaterItem>> translatermap = new HashMap<>();

    public int add(Long userId, TranslaterItem translater) {

        if (translatermap.containsKey(userId)) {

            translatermap.get(userId).add(translater);

            return translatermap.get(userId).size();

        } else {

            List<TranslaterItem> list = new LinkedList<>();

            list.add(translater);

            translatermap.put(userId, list);

            return 1;

        }

    }



    public List<TranslaterItem> getTodoList(Long userId) {

        if (translatermap.containsKey(userId)) {

            return translatermap.get(userId);

        }

        return null;

    }





    public TranslaterItem getItem(Long userId, String id) {

        if (translatermap.containsKey(userId)) {

            List<TranslaterItem> list = translatermap.get(userId);

            for (TranslaterItem translaterItem : list) {

                if (translaterItem.getId().equals(id)) {

                    return translaterItem;

                }

            }



        }

        return null;

    }



    public boolean delete(Long userId, String id) {

        TranslaterItem translaterItem = this.getItem(userId, id);

        if (translaterItem != null) {

            this.translatermap.get(userId).remove(translaterItem);

            return true;

        }

        return false;

    }


}
