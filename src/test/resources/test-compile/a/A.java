package a;

public class A {
    private class InnerClass {
    }

    public A() {
        new InnerClass() {{
            System.out.println("A initialized");
        }};
    }
}
