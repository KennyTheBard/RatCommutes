public class Queue {

    private MappedNode[] nodes;
    private int top;
    private int max;

    public Queue() {
        top = 0;
        max = 2;
        nodes = new MappedNode[max];
    }

    public MappedNode pop() {
        top --;
        MappedNode t = nodes[0];
        for(int i = 1; i <= size(); i++) {
            nodes[i-1] = nodes[i];
        }
        return t;
    }

    public void push(MappedNode node) {
        if(max == top) {
            updateSize();
        }
        nodes[top] = node;
        top ++;
    }

    public MappedNode peek() {
        return nodes[0];
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

    public void printQueue() {
        for(MappedNode mNode : nodes) {
            System.out.print(mNode.getNode().toString() + " ");
        }
        System.out.print("\n");
    }
}