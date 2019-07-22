import express.Express;

public class TransferApplication {

    public static void main(String[] args) {
        Express app = new Express();

        app.get("/", (req, res) -> res.send("Hello World")).listen();
    }
}
