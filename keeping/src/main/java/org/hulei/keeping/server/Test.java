package org.hulei.keeping.server;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: cxx
 * Dom4j解析xml
 * @Date: 2018/5/30 12:21
 */
public class Test {

    public static void main(String[] args) throws Exception {
        // File file = new File("classpath:dictConvert.xml");

        File file = ResourceUtils.getFile("classpath:dictConvert.xml");
        //1.创建Reader对象
        SAXReader reader = new SAXReader();
        //2.加载xml
        Document document = reader.read(file);
        //3.获取根节点
        Element rootElement = document.getRootElement();
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()) {
            Element stu = (Element) iterator.next();
            List<Attribute> attributes = stu.attributes();
            System.out.println("======获取属性值======");
            for (Attribute attribute : attributes) {
                System.out.println(attribute.getValue());
            }
            System.out.println("======遍历子节点======");
            Iterator iterator1 = stu.elementIterator();
            while (iterator1.hasNext()) {
                Element stuChild = (Element) iterator1.next();
                List<Attribute> attributeList = stuChild.attributes();
                attributeList.forEach(i -> {
                    System.out.print(i.getName());
                    System.out.print("-");
                    System.out.print(i.getValue());
                    System.out.println();
                });
            }
        }
    }
}