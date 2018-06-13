package core;

import java.util.ArrayList;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 18-5-22 下午4:06
 * @desc :
 */
public class Storage {

    public static ArrayList<String> storages = new ArrayList<>();

    public static ArrayList<String> getStorages() {
        return storages;
    }

    public static void setStorages(ArrayList<String> storages) {
        Storage.storages = storages;
    }

    public static void add(String storageAddress) {
        storages.add(storageAddress);
    }

    public static String get(int index){
        return storages.get(index);
    }
}
