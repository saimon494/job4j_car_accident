package ru.job4j.di;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringDI {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("ru.job4j.di");
        context.refresh();
        StartUI ui = context.getBean(StartUI.class);
        ui.add("Text1");
        ui.add("Text2");
        ui.print();
        ui.askStr();
        StartUI ui2 = context.getBean(StartUI.class);
        ui2.print();
    }
}
