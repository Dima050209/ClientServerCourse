import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    void shouldSetPriceAndCountCorrectly(){
        double myPrice = 10;
        int myAmount = 100;
        Product apples = new Product("apples", myAmount, myPrice);
        FakeReceiver fakeReceiver = new FakeReceiver();
        int min;
        int max;
        int randomNumber;
        CommandType commandType;
        int addTakeAmount;
        for(int i = 0; i < 100; ++i){
            min = 0;
            max = 1;
            randomNumber = (int)(Math.random()*(max-min+1)+min);
            addTakeAmount = (randomNumber + 10) * 2;
            if(randomNumber == 0){
                commandType = CommandType.TAKE_AMOUNT;
                myAmount -= addTakeAmount;
            } else {
                commandType = CommandType.ADD_AMOUNT;
                myAmount += addTakeAmount;
            }
            Command command1 = new Command(
                    commandType, "user"+randomNumber,
                    addTakeAmount, "apples");
            Decryptor decryptor1 = new Decryptor(fakeReceiver.encrypt(command1));

            min = 1;
            max = 100;
            randomNumber = (int)(Math.random()*(max-min+1)+min);
            Command command2 = new Command(
                    CommandType.SET_PRICE, "user"+randomNumber,
                    randomNumber, "apples");
            myPrice = randomNumber;
            Decryptor decryptor2 = new Decryptor(fakeReceiver.encrypt(command2));
            try {
                decryptor1.join();
                decryptor2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(apples.getAmount(), myAmount);
        Assertions.assertEquals(apples.getPrice(), myPrice);

    }
    @Test
    void shouldPassGroupOperations(){
        FakeReceiver fakeReceiver = new FakeReceiver();
        Product apples = new Product("apples1", 100, 10);
        Product bananas = new Product("bananas", 50, 20);
        Product oranges = new Product("oranges", 200, 5);

        int min;
        int max;
        int randomNumber;
        for(int i = 0; i < 100; ++i) {
            String productId = "";
            min = 0;
            max = 2;
            randomNumber = (int) (Math.random() * (max - min + 1) + min);
            switch (randomNumber) {
                case 0:
                    productId = "apples1";
                    break;
                case 1:
                    productId = "bananas";
                    break;
                case 2:
                    productId = "oranges";
                    break;
                default:
                    break;
            }
            Command command1 = new Command(CommandType.ADD_PRODUCT_GROUP, "user" + randomNumber,
                    i, productId);
            Decryptor decryptor1 = new Decryptor(fakeReceiver.encrypt(command1));
            try {
                decryptor1.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Command command2 = new Command(CommandType.ADD_PRODUCT_TO_GROUP, "user0",
                0, "apples1");
        Decryptor decryptor2 = new Decryptor(fakeReceiver.encrypt(command2));
        Command command3 = new Command(CommandType.ADD_PRODUCT_TO_GROUP, "user0",
                1, "bananas");
        Decryptor decryptor3 = new Decryptor(fakeReceiver.encrypt(command3));
        Command command4 = new Command(CommandType.ADD_PRODUCT_TO_GROUP, "user0",
                2, "oranges");
        Decryptor decryptor4 = new Decryptor(fakeReceiver.encrypt(command4));
        try {
            decryptor2.join();
            decryptor3.join();
            decryptor4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(apples.getProductGroup(), "Group0");
        Assertions.assertEquals(bananas.getProductGroup(), "Group1");
        Assertions.assertEquals(oranges.getProductGroup(), "Group2");
    }
}
