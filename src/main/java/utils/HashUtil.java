package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
 * @author : Bruce Zhao
 * @email : zhzh402@163.com
 * @date : 2018/5/17 16:15
 * @desc : 一致性hash算法，没有虚拟节点的实现
 */
public class HashUtil {

    private SortedMap<Integer, String> dataServers = null;


    public void HashUtil(){}

    public void init(ArrayList<String> servers){
        dataServers = new TreeMap<Integer, String>();
        for (int i = 0; i < servers.size(); i++) {
            int hash = getHash(servers.get(i));
            System.out.println("[" + servers.get(i) + "]加入， hash value: " + hash);
            dataServers.put(hash, servers.get(i));
        }
    }

    public SortedMap<Integer, String> getDataServers() {
        return dataServers;
    }


    public static int getHash(String serverIpStr) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < serverIpStr.length(); i++)
            hash = (hash ^ serverIpStr.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }



    public static void main(String[] args) {


        /*Iterator<Map.Entry<Integer, String>> iterator = dataServers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> next = iterator.next();
            System.out.println(next.getKey() + " " + next.getValue());
        }


        for (int i = 0; i < nodes.length; i++) {
            System.out.println("[" + nodes[i] + "]的hash值为: " + getHash(nodes[i]) + ", 被分配到: " + getServer(nodes[i]));
        }
*/

//        System.out.println(getServer(""));

        return;
    }

    public String getServer(String node) {
        HashUtil hashUtil = new HashUtil();
        int hash = getHash(node);

        SortedMap<Integer, String> biggerNodes = hashUtil.getDataServers().tailMap(hash);
        if (biggerNodes.size() == 0) {
            return hashUtil.getDataServers().get(hashUtil.getDataServers().firstKey());
        }
        return biggerNodes.get(biggerNodes.firstKey());


//        return  "";
    }
}
