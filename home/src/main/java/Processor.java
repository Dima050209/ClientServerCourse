public class Processor extends Thread{
    Command command;
    public Processor(Command command) {
        this.command = command;
        this.start();
    }
    @Override
    public void run(){
        super.run();
        process();
    }

    public void process() {
        System.out.println(command);
        CommandType commandId = command.getCommandId();
        String userId = command.getUserId();
        String productId = command.getProductId() + "";
        double commandBody = command.getCommandBody();

        switch (commandId){
            case ADD_AMOUNT:
                Product.getProductById(productId).addAmount((int)commandBody);
                break;
            case GET_AMOUNT:
                Product.getProductById(productId).getAmount();
                break;
            case SET_PRICE:
                Product.getProductById(productId).setPrice(commandBody);
                break;
            case TAKE_AMOUNT:
                Product.getProductById(productId).takeAmount((int)commandBody);
                break;
            case ADD_PRODUCT_GROUP:
                Product.addProductGroup("Group"+(int)commandBody);
                break;
            case ADD_PRODUCT_TO_GROUP:
                Product.getProductById(productId).setProductGroup("Group"+(int)commandBody);
                break;
            default:
                break;
        }
        Command okCommand = new Command(CommandType.OK,userId,commandBody,productId);
        Encryptor encryptor = new Encryptor(okCommand);
    }
}
