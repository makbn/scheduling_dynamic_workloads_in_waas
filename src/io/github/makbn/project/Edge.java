package io.github.makbn.project;

public class Edge implements Comparable<Edge>{
    private Task parent;
    private Task child;


    public Task getParent() {
        return parent;
    }



    public Task getChild() {
        return child;
    }


    public Edge(Task parent, Task child) {
        this.parent = parent;
        this.child = child;
    }

    @Override
    public int compareTo(Edge o) {
        if(o.getParent()!=this.parent
                || o.getChild()!=this.child)
            return -1;
        return 0;
    }


    @Override
    public boolean equals(Object edge) {
        if(((Edge)edge).getChild().equals(this.child)
                && ((Edge)edge).getParent().equals(this.parent)){
            return true;
        }
        return false;
    }
}