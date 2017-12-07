package servlet;

public class test {
    public static void main(String[] args) throws  Exception{
    Chatmq chatmq =  new Chatmq();
    chatmq.Recv();
    chatmq.Send();
    }
}