package TelegramBotLearning;

import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.A;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestBot extends TelegramLongPollingBot{
    public static String readTxt(File f) throws FileNotFoundException {
        String answer = "";
        Scanner scan = new Scanner(f);
        while (scan.hasNextLine()){
            answer = answer + scan.nextLine() + "\n";
        }
        scan.close();
        return answer;
    }
    public static void saveTxt(String s, File f) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(f);
        String[] a = s.split(", ");
        for(String b : a){
            pw.println(b);
        }
        pw.close();
    }

    String[] daysOfWeak = new String[]{"ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА"};
    File[] files = new File[]{new File("ПутьДоФайла.txt"),
            new File("ПутьДоФайла.txt"),
            new File("ПутьДоФайла.txt"),
            new File("ПутьДоФайла.txt"),
            new File("ПутьДоФайла.txt"),
            new File("ПутьДоФайла.txt"),
    };

    @SneakyThrows
    public void sendMsg(Message message, String TEXT){ // Сделал метод sendMsg для отправки сообщений(по стандарту он слишком длинный)
        SendMessage sendMessage = new SendMessage(); // Создаем новый объект класса SendMessage
        sendMessage.enableMarkdown(true);// Мы можем отправлять боту сообщение
        sendMessage.setChatId(message.getChatId().toString());// Берем с отправденного сообщения ID чата(чтобы узнать, кто отправляет сообщение)
        sendMessage.setReplyToMessageId(message.getMessageId());// Берем ID сообшения
        sendMessage.setText(TEXT);// Береи текст с сообщения

        setButtons(sendMessage);

        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e){
            e.printStackTrace();
        }
        //execute(SendMessage.builder().chatId(ID).text(TEXT).build());
    }

    // Метод с добавлением клавиатуры(кнопок)
    public void setButtons(SendMessage SMsg){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();// Создаем клавиатуру
        List<KeyboardRow> keyboardRowList = new ArrayList<>();// Создаем список с вертикальными рядами в клавиатуре

        KeyboardRow keyboardRow1 = new KeyboardRow();// Создаем первую строчку в клавиатуре
        KeyboardRow keyboardRow2 = new KeyboardRow();

        KeyboardButton keyboardButton = new KeyboardButton();// Создаем кнопку в клавиатуре
        keyboardButton.setText("ПОНЕДЕЛЬНИК");// Добавляем текст в кнопку
        keyboardRow1.add(keyboardButton);// Добавляем кнопку в первую строчу

        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton2.setText("ВТОРНИК");
        keyboardRow1.add(keyboardButton2);

        KeyboardButton keyboardButton3 = new KeyboardButton();
        keyboardButton3.setText("СРЕДА");
        keyboardRow1.add(keyboardButton3);

        KeyboardButton keyboardButton4 = new KeyboardButton();// Создаем кнопку в клавиатуре
        keyboardButton4.setText("ЧЕТВЕРГ");// Добавляем текст в кнопку
        keyboardRow2.add(keyboardButton4);// Добавляем кнопку в первую строчу

        KeyboardButton keyboardButton5 = new KeyboardButton();
        keyboardButton5.setText("ПЯТНИЦА");
        keyboardRow2.add(keyboardButton5);

        KeyboardButton keyboardButton6 = new KeyboardButton();
        keyboardButton6.setText("СУББОТА");
        keyboardRow2.add(keyboardButton6);


        keyboardRowList.add(keyboardRow1);// Добавляем в список строчек первую строчку
        keyboardRowList.add(keyboardRow2);// Добавляем в список строчек вторую строчку

        replyKeyboardMarkup.setKeyboard(keyboardRowList);// Добавлям в клавиатуру список строчек
        SMsg.setReplyMarkup(replyKeyboardMarkup);// Сохраняем клавиатуру
    }

    // Метод с реакциями на обновления
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update){
        int index = 0;
        Message message = update.getMessage(); // Сообщение = сообщение из обновления
        if(message != null && message.hasText()){ // Если сообщение не null и сообщение имеет текст
            switch(message.getText()){ // И свитчем делаем реакции на сообщения
                case "ПОНЕДЕЛЬНИК":
                case "ВТОРНИК":
                case "СРЕДА":
                case "ЧЕТВЕРГ":
                case "ПЯТНИЦА":
                case "СУББОТА":
                    for(int i = 0; i < daysOfWeak.length; i++){
                        if(message.getText().equals(daysOfWeak[i])){
                            index = i;
                        }
                    }
                    sendMsg(message, readTxt(files[index]));
                    break;

                default:
                    String[] a = message.getText().split(": ");
                    for(int i = 0; i < daysOfWeak.length; i++){
                        if(a[0].equals(daysOfWeak[i])){
                            index = i;
                        }
                    }
                    saveTxt(a[1], files[index]);

            }
        }
    }

    // Даем боту имя
    @Override
    public String getBotUsername(){ // Даем боту имя
        return "Test bot";
    }

    // Даем боту токен
    @Override
    public String getBotToken() {
        return "5585005397:AAGek-X-Gc9CFESFBPrFFr7vlvbkYj5QUNk";
    }

    @SneakyThrows
    public static void main(String[] args) {
        // Создаем бота
        TestBot bot = new TestBot();
        // Регистрируем бота
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);


    }
}

