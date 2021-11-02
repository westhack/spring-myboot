package com.limaopu.myboot.core.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 树形工具类
 */
@Getter
@Setter
@Data
public class TreeUtil {

    /**
     * 级数
     */
    private int layer;
    private String id;
    private String name;
    private String parentId;
    /**
     * 是否开启 默认开启
     */
    private boolean open = true;
    /**
     * 是否选择 checkbox状态可用 默认未选中
     */
    private boolean checked = false;
    /**
     * 排序字段
     */
    private Integer orderNum = 0;
    private List<TreeUtil> children = new ArrayList<>();
    /**
     * 数据
     */
    private Object data = null;

    public TreeUtil() {
    }

    public TreeUtil(String id, String name, String parentId, Integer orderNum) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.orderNum = orderNum;
    }

    /**
     * @param allList 全部列表
     * @param checkeds 已经选中ID
     * @return JSONArray
     */
    static public JSONArray getTree(List<TreeUtil> allList, HashSet<String> checkeds) {
        TreeUtil treeUtil;
        List<TreeUtil> parents = allList.stream().filter(list ->
                        StrUtil.isEmpty(list.getParentId()))
                .collect(Collectors.toList());
        allList.removeAll(parents);
        parents.sort(Comparator.comparingInt(TreeUtil::getOrderNum));
        JSONArray jsonArr = new JSONArray();
        for (TreeUtil parent : parents) {
            treeUtil = getChildByTree(parent, allList, 0, checkeds);
            /*判断是否存在*/
            if (checkeds != null && checkeds.size() > 0 && checkeds.contains(parent.getId())) {
                treeUtil.setChecked(true);
            }
            jsonArr.add(treeUtil);
        }
        return jsonArr;
    }

    /**
     *
     * @param parent 上级
     * @param allList 全部列表
     * @param layer 深度
     * @param checkeds 已经选中ID
     * @return TreeUtil
     */
    static private TreeUtil getChildByTree(TreeUtil parent, List<TreeUtil> allList, int layer, HashSet<String> checkeds) {
        layer++;
        List<TreeUtil> children = allList.stream().filter(s ->
                s.getParentId().equals(parent.getId()) ).collect(Collectors.toList());
        allList.removeAll(children);
        /*判断是否存在*/
        if (checkeds != null && checkeds.size() > 0 && checkeds.contains(parent.getId())) {
            parent.setChecked(true);
        }
        for (TreeUtil child : children) {
            TreeUtil m = getChildByTree(child, allList, layer, checkeds);
            parent.getChildren().add(m);
        }

        List<TreeUtil> children1 = parent.getChildren();
        if (children1.size() > 0) {
            children1.sort(Comparator.comparingInt(TreeUtil::getOrderNum));
            parent.setChildren(children1);
        }

        return parent;
    }

    /**
     * 任意对象转树形
     * @param allList
     * @param parentKey
     * @param idKey
     * @param nameKey
     * @param orderKey
     * @param checkeds
     * @param <T>
     * @return JSONArray
     */
    static public <T> JSONArray objectToTree(List<T>  allList, String parentKey, String idKey, String nameKey, String orderKey, HashSet<String> checkeds) {
        List<TreeUtil> toList = toList(allList, parentKey, idKey, nameKey, orderKey);
        JSONArray jsonArr = getTree(toList, checkeds);
        return jsonArr;
    }

    /**
     * 对象转 List<TreeUtil>
     * @param allList
     * @param parentKey
     * @param idKey
     * @param nameKey
     * @param orderKey
     * @param <T>
     * @return
     */
    public static <T> List<TreeUtil> toList(List<T>  allList, String parentKey, String idKey, String nameKey, String orderKey) {

        List<TreeUtil> toList = new ArrayList<>();

        for (Object o : allList) {
            Map<String, Object> map = BeanUtil.beanToMap(o);
            System.out.println(map);
            TreeUtil treeUtil = new TreeUtil();
            treeUtil.setId((String) map.get(idKey));
            treeUtil.setName((String) map.get(nameKey));
            treeUtil.setLayer(0);
            treeUtil.setParentId((String) map.get(parentKey));

            if (!StrUtil.isEmpty(orderKey)) {
                treeUtil.setOrderNum(Integer.parseInt((String) map.get(orderKey)));
            } else {
                treeUtil.setOrderNum(0);
            }
            treeUtil.setData(map);
            toList.add(treeUtil);
        }

        return toList;
    }

    /**
     * list map 转 树形
     * @param allList
     * @param parentKey
     * @param idKey
     * @param nameKey
     * @param orderKey
     * @param checkeds
     * @return
     */
    static public JSONArray mapToTree(List<Map<String, Object>> allList, String parentKey, String idKey, String nameKey, String orderKey, HashSet<String> checkeds) {
        TreeUtil treeUtil;
        List<Map<String, Object>> parents = allList.stream().filter(map -> StrUtil.isEmpty((String)map.get(parentKey))).collect(Collectors.toList());
        allList.removeAll(parents);
        parents.sort(new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                if (StrUtil.isEmpty(orderKey)) {
                    return 0;
                }

               int order1 = Integer.parseInt((String) o1.get(orderKey));
               int order2 = Integer.parseInt((String) o2.get(orderKey));
                return order1 - order2; // 越小越靠前
            }
        });
        JSONArray jsonArr = new JSONArray();
        for (Map<String, Object> parent : parents) {
            Map<String, Object> tree = getChildByTree(parent, allList, parentKey, idKey, nameKey, orderKey, 0, checkeds);
            /*判断是否存在*/
            if (checkeds != null && checkeds.size() > 0 && checkeds.contains(parent.get(idKey))) {
                tree.put("checked", true);
            } else {
                tree.put("checked", false);
            }
            tree.put("layer", 0);
            jsonArr.add(tree);
        }
        return jsonArr;
    }

    static private Map<String, Object> getChildByTree(Map<String, Object> parent, List<Map<String, Object>> allList, String parentKey, String idKey, String nameKey, String orderKey, int layer, HashSet<String> checkeds) {
        layer++;
        List<Map<String, Object>> children = allList.stream()
                .filter(s -> ((String)s.get(parentKey)).equals((String)parent.get(idKey)) )
                .collect(Collectors.toList());
        allList.removeAll(children);
        /*判断是否存在*/
        if (checkeds != null && checkeds.size() > 0 && checkeds.contains( (String)parent.get(idKey) )) {
            parent.put("checked", true);
        } else {
            parent.put("checked", false);
        }

        Comparator<Map<String, Object>> cp = new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {

                if (StrUtil.isEmpty(orderKey)) {
                    return 0;
                }

                int order1 = Integer.parseInt((String) o1.get(orderKey));
                int order2 = Integer.parseInt((String) o2.get(orderKey));
                return order1 - order2; // 越小越靠前
            }
        };

        List<Map<String, Object>> c = new ArrayList<>();
        for (Map<String, Object> child : children) {
            Map<String, Object> m = getChildByTree(child, allList, parentKey, idKey, nameKey, orderKey, layer, checkeds);
            c.add(m);
        }

        if (c.size() > 0) {
            c.sort(cp);
        }
        parent.put("children", c);

        parent.put("layer", layer);
        return parent;
    }

    @Data
    public static class Node {
        public Node (String id, String lable, String pId) {
            this.id = id;
            this.lable = lable;
            this.pId = pId;
        }
        private String id;
        private String lable;
        private String pId;
        private String url;
    }

    public static void main(String[] args) {

        List<TreeUtil> allList = new ArrayList<>();

        allList.add(new TreeUtil("1", "分类1", "", 1));
        allList.add(new TreeUtil("2", "分类2", "", 2));
        allList.add(new TreeUtil("3", "分类3", "", 3));

        allList.add(new TreeUtil("4", "分类4", "1", 2));
        allList.add(new TreeUtil("5", "分类5", "1", 1));
        allList.add(new TreeUtil("6", "分类6", "1", 9));
        allList.add(new TreeUtil("7", "分类7", "5", 8));
        allList.add(new TreeUtil("8", "分类8", "7", 8));
        allList.add(new TreeUtil("9", "分类9", "2", 10));
        allList.add(new TreeUtil("10", "分类10", "2", 9));

        System.out.println(allList);

        JSONArray treeStr = getTree(allList, null);

        System.out.println(treeStr);


        List<Node> nodes = new ArrayList<>();

        nodes.add(new Node("1", "分类1", ""));
        nodes.add(new Node("2", "分类2", ""));
        nodes.add(new Node("3", "分类3", ""));

        nodes.add(new Node("4", "分类4", "1"));
        nodes.add(new Node("5", "分类5", "1"));
        nodes.add(new Node("6", "分类6", "1"));
        nodes.add(new Node("7", "分类7", "5"));
        nodes.add(new Node("8", "分类8", "7"));
        nodes.add(new Node("9", "分类9", "2"));
        nodes.add(new Node("10", "分类10", "2"));

        JSONArray treeStr2 = objectToTree(nodes, "pId", "id", "lable", null, null);

        System.out.println(treeStr2);

        List<Map<String, Object>> nodes2 = new ArrayList<>();

        nodes2.add(BeanUtil.beanToMap(new Node("1", "分类1", "")));
        nodes2.add(BeanUtil.beanToMap(new Node("2", "分类2", "")));
        nodes2.add(BeanUtil.beanToMap(new Node("3", "分类3", "")));

        nodes2.add(BeanUtil.beanToMap(new Node("4", "分类4", "1")));
        nodes2.add(BeanUtil.beanToMap(new Node("5", "分类5", "1")));
        nodes2.add(BeanUtil.beanToMap(new Node("6", "分类6", "1")));
        nodes2.add(BeanUtil.beanToMap(new Node("7", "分类7", "5")));
        nodes2.add(BeanUtil.beanToMap(new Node("8", "分类8", "7")));
        nodes2.add(BeanUtil.beanToMap(new Node("9", "分类9", "2")));
        nodes2.add(BeanUtil.beanToMap(new Node("10", "分类10", "2")));

        HashSet<String> checkds = new HashSet();
        checkds.add("1");
        checkds.add("2");
        JSONArray treeStr3 = mapToTree(nodes2, "pId", "id", "lable", "id", checkds);
        System.out.println("     " + treeStr3);
    }
}