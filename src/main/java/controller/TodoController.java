package controller;

import dto.CodeMessage;
import dto.TodoItem;
import enums.CodeMessageType;
import enums.TodoItemType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import repository.TodoRepository;
import utils.InlineButtonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoController {

    private Map<Long, TodoItem> todoItemStep = new HashMap<>();
    private final TodoRepository todoRepository = new TodoRepository();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public CodeMessage handle(String text, Long chatId, Integer messageId) {
        CodeMessage codeMessage = new CodeMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));


        if (text.startsWith("/todo/")) {
            String[] commandList = text.split("/");
            String command = commandList[2];

            if (command.equals("menu")) {
                EditMessageText editMessageText = new EditMessageText();

                editMessageText.setText("<b><i>ToDo Menu</i></b>");

                editMessageText.setChatId(String.valueOf(chatId));

                editMessageText.setMessageId(messageId);

                editMessageText.setParseMode("HTML");


                editMessageText.setReplyMarkup(todoMenu());


                codeMessage.setType(CodeMessageType.EDIT);

                codeMessage.setEditMessageText(editMessageText);
                return codeMessage;
            }
            switch (command) {
                case "list" -> {
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setParseMode("HTML");

                    List<TodoItem> todoItemList = this.todoRepository.getTodoList(chatId);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (todoItemList == null || todoItemList.isEmpty()) {
                        stringBuilder.append("You do not have any todo list");
                    } else {
                        int count = 1;
                        for (TodoItem dto : todoItemList) {
                            stringBuilder.append("ID: <b>").append(count).append("</b>");
                            stringBuilder.append("\n");
                            stringBuilder.append("<b>Title: </b>").append(dto.getTitle());
                            stringBuilder.append("\n");
                            stringBuilder.append("<b>Content: </b>").append(dto.getContent());
                            stringBuilder.append("\n");
                            stringBuilder.append("<b>ToDo yaratilgan vaqt: </b>").append(simpleDateFormat.format(dto.getCreatedDate())); //
                            stringBuilder.append(" /todo_edit_").append(dto.getId());
                            stringBuilder.append("\n\n");
                            count++;
                        }
                    }

                    editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(
                            InlineButtonUtil.collection(
                                    InlineButtonUtil.row(InlineButtonUtil.button("Back to menu", "/todo/menu", ":back:"))
                            )));

                    editMessageText.setText(stringBuilder.toString());

                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(CodeMessageType.EDIT);

                }
                case "create" -> {
                    EditMessageText editMessageText = new EditMessageText();

                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setText("Send *Title* ");
                    editMessageText.setParseMode("Markdown");
                    editMessageText.setMessageId(messageId);


                    TodoItem todoItem = new TodoItem();
                    todoItem.setId(String.valueOf(messageId));
                    todoItem.setUserId(chatId);
                    todoItem.setType(TodoItemType.TITLE);

                    this.todoItemStep.put(chatId, todoItem);


                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(CodeMessageType.EDIT);
                }
                case "update" -> {
                    command = commandList[3];
                    String id = commandList[4];

                    TodoItem todoItem = this.todoRepository.getItem(chatId, id);
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setParseMode("Markdown");

                    // todoItem == null

                    if (command.equals("title")) {
                        editMessageText.setText("*Current Title* " + todoItem.getTitle() + "\nPlease send new Title.");

                        codeMessage.setEditMessageText(editMessageText);
                        codeMessage.setType(CodeMessageType.EDIT);
                        todoItem.setType(TodoItemType.UPDATE_TITLE);
                        todoItemStep.put(chatId, todoItem);
                    } else if (command.equals("content")) {
                        editMessageText.setText("*Current Content* " + todoItem.getContent() + "\nPlease send new Content.");

                        codeMessage.setEditMessageText(editMessageText);
                        codeMessage.setType(CodeMessageType.EDIT);
                        todoItem.setType(TodoItemType.UPDATE_CONTENT);
                        todoItemStep.put(chatId, todoItem);
                    }


                    editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(InlineButtonUtil.collection(
                            InlineButtonUtil.row(InlineButtonUtil.button("Cancel", "/todo/cancel")))));

                }
                case "cancel" -> {
                    this.todoItemStep.remove(chatId);
                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));
                    editMessageText.setText("Update Was Cancelled");
                    editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(
                            InlineButtonUtil.collection(
                                    InlineButtonUtil.row(InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:")),
                                    InlineButtonUtil.row(InlineButtonUtil.button("Go to Menu", "menu"))
                            )));

                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(CodeMessageType.EDIT);
                }
                case "delete" -> {
                    String id = commandList[3];
                    boolean result = this.todoRepository.delete(chatId, id);

                    EditMessageText editMessageText = new EditMessageText();
                    editMessageText.setMessageId(messageId);
                    editMessageText.setChatId(String.valueOf(chatId));

                    if (result) {
                        editMessageText.setText("Todo was Deleted");
                    } else {
                        editMessageText.setText("ERROR");
                    }

                    editMessageText.setReplyMarkup(InlineButtonUtil.keyboard(
                            InlineButtonUtil.collection(
                                    InlineButtonUtil.row(InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:")),
                                    InlineButtonUtil.row(InlineButtonUtil.button("Back to menu", "/todo/menu", ":back:"))
                            )));

                    codeMessage.setEditMessageText(editMessageText);
                    codeMessage.setType(CodeMessageType.EDIT);

                }
            }

            return codeMessage;
        }

        if (text.startsWith("/todo_")) {
            String todoId = text.split("/todo_edit_")[1];
            TodoItem todoItem = this.todoRepository.getItem(chatId, todoId);
            if (todoItem == null) {
                sendMessage.setText("Voy jo'ra bu qatdan keldi. No ID FOUND");
            } else {
                sendMessage.setText(todoItem.getTitle() + "\n" + todoItem.getContent() + "\n" + "_" + simpleDateFormat.format(todoItem.getCreatedDate()) + "_");
                sendMessage.setParseMode("Markdown");
                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            }

            codeMessage.setSendMessage(sendMessage);
            codeMessage.setType(CodeMessageType.MESSAGE);

            return codeMessage;
        }

        if (this.todoItemStep.containsKey(chatId)) {
            TodoItem todoItem = this.todoItemStep.get(chatId);

            sendMessage.setParseMode("Markdown");
            codeMessage.setSendMessage(sendMessage);
            codeMessage.setType(CodeMessageType.MESSAGE);

            if (todoItem.getType().equals(TodoItemType.TITLE)) {
                todoItem.setTitle(text);
                sendMessage.setText("*Title* " + todoItem.getTitle() + "\n" + " Send *Content*: ");
                todoItem.setType(TodoItemType.CONTENT);
            } else if (todoItem.getType().equals(TodoItemType.CONTENT)) {
                todoItem.setContent(text);
                todoItem.setCreatedDate(new Date());
                todoItem.setType(TodoItemType.FINISHED);
                int n = this.todoRepository.add(chatId, todoItem);
                todoItemStep.remove(chatId);


                sendMessage.setText(" ItemCount  " + n + "\n*Title* " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent() + "\n"
                        + "_Create Todo finished._");

                sendMessage.setReplyMarkup(InlineButtonUtil.keyboard(
                        InlineButtonUtil.collection(
                                InlineButtonUtil.row(InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:")),
                                InlineButtonUtil.row(InlineButtonUtil.button("Back to menu", "/todo/menu", ":back:"))
                        )));


            } else if (todoItem.getType().equals(TodoItemType.UPDATE_TITLE)) {
                todoItem.setTitle(text);
                this.todoItemStep.remove(chatId);
                // this.todoRepository.getItem(chatId, todoItem.getId());
                sendMessage.setText("*Title* " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent());
                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            } else if (todoItem.getType().equals(TodoItemType.UPDATE_CONTENT)) {
                todoItem.setContent(text);
                this.todoItemStep.remove(chatId);
                // this.todoRepository.getItem(chatId, todoItem.getId());
                sendMessage.setText("*Title* " + todoItem.getTitle() + "\n" + "*Content*: " + todoItem.getContent());
                sendMessage.setReplyMarkup(getTodoItemKeyBoard(todoItem.getId()));
            }
        }

        return codeMessage;
    }


    private InlineKeyboardMarkup getTodoItemKeyBoard(String id) {
        List<List<InlineKeyboardButton>> rowCollection =
                InlineButtonUtil.collection(
                        InlineButtonUtil.row(
                                InlineButtonUtil.button("Update Title", "/todo/update/title/" + id),
                                InlineButtonUtil.button("Update Content", "/todo/update/content/" + id),
                                InlineButtonUtil.button("Delete", "/todo/delete/" + id, ":x:")),
                        InlineButtonUtil.row(InlineButtonUtil.button("ToDo List", "/todo/list", ":clipboard:"))

                );

        return InlineButtonUtil.keyboard(rowCollection);
    }

    public Map<Long, TodoItem> getTodoItemStep() {
        return todoItemStep;
    }

    public void setTodoItemStep(Map<Long, TodoItem> todoItemStep) {
        this.todoItemStep = todoItemStep;
    }

    public InlineKeyboardMarkup todoMenu() {
        List<List<InlineKeyboardButton>> rowCollection =

                InlineButtonUtil.collection(

                        InlineButtonUtil.row(

                                InlineButtonUtil.button("\uD835\uDE3E\uD835\uDE67\uD835\uDE5A\uD835\uDE56\uD835\uDE69\uD835\uDE5A", "/todo/create/", "➕")),

                        InlineButtonUtil.row(InlineButtonUtil.button("\uD835\uDE4F\uD835\uDE64\uD835\uDE3F\uD835\uDE64 \uD835\uDE47\uD835\uDE5E\uD835\uDE68\uD835\uDE69", "/todo/list", ":clipboard:")),
                        InlineButtonUtil.row(InlineButtonUtil.button("Back to menu", "menu", ":back:"))


                );


        return InlineButtonUtil.keyboard(rowCollection);

    }

}
