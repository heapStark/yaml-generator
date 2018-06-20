# 一个简单的yaml文档生成工具
```
之前在京东云参与接入OpenApi网关改造,需要提供yaml文档给网关组生成sdk
为了减少工作量以及后面方便维护,实现了一个简单的yaml文档生成工具
```
```
由于泛型的问题，当前版本仅支持jdk1.8以及spring4.2
所有controller类应当满足示例代码中的注解规范
由于基于包扫描查找controller,mvn仓库中应当首先install对应的工程,
可以增加一个单独的profile引入插件以及插件依赖
``` 
## 一个简单的示例工程

[示例代码](https://github.com/heapStark/yaml-generator-demo)
##
步骤
1.  mvn package install -Papi-package
2.  mvn install -Papi-****
## 更新说明
当前已经支持不需要显示添加@RequestParam注解
由于大部分工程打包方式为war包,pom文件在需要显示指定
## todoList & problem
不支持在yaml文档中输出注释
解决方案1：定义注释注解,这种方案对代码侵入太高
## 使用说明,一个pom文件实例

```
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
            <plugin>
             <groupId>heap-stark</groupId>
             <artifactId>yaml-generator</artifactId>
             <version>1.0-SNAPSHOT</version>
             <configuration>
                 <controllerPackage>service.test</controllerPackage>
                 <resultPath>/home/wzl/source/demo/linux
                 </resultPath>
                 <moduleName>test</moduleName>
             </configuration>
             <dependencies>
                 <dependency>
                     <groupId>heap-stark</groupId>
                     <artifactId>yaml-web</artifactId>
                     <version>1.0-SNAPSHOT</version>
                 </dependency>
                 <dependency>
                     <groupId>heap-stark</groupId>
                     <artifactId>yaml-web2</artifactId>
                     <version>1.0-SNAPSHOT</version>
                 </dependency>
             </dependencies>
             <executions>
                 <execution>
                     <id>first</id>
                     <phase>package</phase>
                     <goals>
                         <goal>yaml</goal>
                     </goals>
                 </execution>
             </executions>
         </plugin>
        </plugins>
    </build>
```


