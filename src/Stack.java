public class Stack{

    private MappedNode[] nodes;
    private int top;
    private int max;

    public Stack() {
        top = 0;
        max = 2;
        nodes = new MappedNode[max];
    }

    public MappedNode pop() {
        if(size() == 0) {
            return null;
        }
        top --;
        return nodes[top];
    }

    public void push(MappedNode task) {
        if(max == top) {
            updateSize();
        }
        nodes[top] = task;
        top ++;
    }

    public MappedNode peek() {
        if(top > 0) {
            return nodes[top - 1];
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

    private void updateSize() {
        int len = size();
        max += max/2;
        MappedNode[] aux = new MappedNode[max];
        System.arraycopy(nodes,0,aux,0,len);
        nodes = aux;
    }

    public void printStack() {
        for(MappedNode mNode : nodes) {
            System.out.print(mNode.getNode().toString() + " ");
        }
        System.out.print("\n");
    }
}