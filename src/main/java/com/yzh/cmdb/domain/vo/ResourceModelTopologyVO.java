package com.yzh.cmdb.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 模型拓扑
 *
 * @author yuanzhihao
 * @since 2024/5/31
 */
@Data
@Builder
public class ResourceModelTopologyVO {
    private List<Node> nodes;
    private List<Edge> edges;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node {
        /**
         * 模型id
         */
        private String id;
        /**
         * 模型名称
         */
        private String label;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Edge {
        /**
         * 源端模型id
         */
        private String source;
        /**
         * 目标端模型id
         */
        private String target;
        /**
         * 模型关联关系
         */
        private String label;
    }

}