# ccte
High Performance Template Engine

for循环输出

    <cc-pagination for="Map _p:pagination">
      <a>${_p.label}</a>
    </cc-pagination>

repeat循环输出

    <cc-pagination>
      <a repeat="Map _p:pagination">${_p.label}</a>
    </cc-pagination>

if else_if else条件判断输出

    <cc-pagination for="Map _p:pagination">
      <a href="${_p.href}/home.xhtml" if="_p.href!=null&&_p.active!=null" active>${_p.label}</a>
      <a href="${_p.href}/home.xhtml" else_if="_p.href!=null">${_p.label}</a>
      <a else_if="_p.active!=null">${_p.label}</a>
      <a else>${_p.label}</a>
    </cc-pagination>

include包含指令用来布局

    <head lang="en">
      <!-- src指向文件名即可，不用管路径 -->
      <include src="head.html"/>
      <script src="xxx.js"></script>
    </head>

    <footer>
      <include src="footer.html"/>
    </footer>

cc_x 通用指令（类似于Angularjs的ng-class，但扩展了）

    <tbody for="MapBean _md:models">
      <tr cc_class="{true:'even',false:'odd'}($index%2==1)">${$index}</tr>
    </tbody>

import导入依赖类

    <import>
      java.xxx.x,
      com.x.*
    </import>

[PerformanceTest](http://ccloomi.github.io/ccte-ptest.html)
