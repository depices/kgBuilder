## 技术栈
前端技术栈 vue + d3.js + node,后台是springboot+Neo4j+mysql.

## 项目结构

|-- kgBuilder-base/                   // 基础功能，一些工具类在里面

|-- kgBuilder-meta/                    // 数据源，E-R图生成图谱所用

|-- kgBuilder-ui/                         //前端界面

|-- kgBuilder-pro/                       //核心应用层

## 已经实现的基本功能:
1. 新增节点,添加连线,快速添加节点和关系
2. 节点的颜色和大小可修改
3. 节点和关系的编辑,删除
4. 导出成图片
5. 导入导出csv数据
6. 节点之间多个关系
7. 案件预定义关系自动抽取

## 后续工作:
1. 页面逻辑调整及美化
2. 新增知识图谱补充与法律判决预测等功能模块

## 环境需求(不需要必须一致，但是要求要找到各个程序互相对应的版本)
- jdk 1.8.0
- neo4j  3.5.5
- mysql 8.0.33
- node 14.16.1

## 项目启动

1. navicat运行项目当中的kg_builder.sql构建数据库

2. 启动neo4j：在neo4j的bin目录下打开命令窗口然后执行下面的命令启动 `neo4j.bat console`

3. idea当中运行项目

4. 前端启动：idea当中打开Terminal

   `cd .\kgBuilder-ui\` 

   `npm install`

   `npm run serve`

   
