package repository;


import dto.TodoItem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


    public class TodoRepository {



        private Map<Long, List<TodoItem>> todoMap = new HashMap<>();

        public int add(Long userId, TodoItem todoItem) {

            if (todoMap.containsKey(userId)) {

                todoMap.get(userId).add(todoItem);

                return todoMap.get(userId).size();

            } else {

                List<TodoItem> list = new LinkedList<>();

                list.add(todoItem);

                todoMap.put(userId, list);

                return 1;

            }

        }



        public List<TodoItem> getTodoList(Long userId) {

            if (todoMap.containsKey(userId)) {

                return todoMap.get(userId);

            }

            return null;

        }





        public TodoItem getItem(Long userId, String id) {

            if (todoMap.containsKey(userId)) {

                List<TodoItem> list = todoMap.get(userId);

                for (TodoItem todoItem : list) {

                    if (todoItem.getId().equals(id)) {

                        return todoItem;

                    }

                }



            }

            return null;

        }



        public boolean delete(Long userId, String id) {

            TodoItem todoItem = this.getItem(userId, id);

            if (todoItem != null) {

                this.todoMap.get(userId).remove(todoItem);

                return true;

            }

            return false;

        }



    }

//    public int save(Long chatID, TodoItem todoItem) {
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter("todoItem.txt", true);
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//            String s = String.join("#", todoItem.getId(), todoItem.getTitle(),
//                    todoItem.getContent(), todoItem.getCreatedDate().toString());
//            printWriter.println(s);
//
//            printWriter.flush();
//            printWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//
//    public List<TodoItem> getList(Long chatId) {
//        FileReader fileReader = null;
//        List<TodoItem> smsList = new LinkedList<>();
//        try {
//            fileReader = new FileReader("sms.txt");
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            String line = bufferedReader.readLine();
//
//            while (line != null) {
//                String[] arr = line.split("#");
//                TodoItem sms = new TodoItem();
//                sms.setId(arr[0]);
//                sms.setTitle(arr[1]);
//                sms.setContent(arr[2]);
////                    sms.setCreatedDate(LocalDateTime.parse(arr[3]));
//                smsList.add(sms);
//                line = bufferedReader.readLine();
//            }
//            bufferedReader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (TodoItem todoItem : smsList) {
//            if(todoItem.equals(chatId)) {
//               return smsList;
//            }
//        }
//        return smsList;
//    }
//
//
//    public boolean delete(Long userId, String id) {
//        try {
//            File inputFile = new File("myFile.txt");
//            File tempFile = new File("myTempFile.txt");
//
//            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//            String lineToRemove = "id";
//            String currentLine;
//
//            while ((currentLine = reader.readLine()) != null) {
//                // trim newline when comparing with lineToRemove
//                String trimmedLine = currentLine.trim();
//                if (trimmedLine.equals(lineToRemove)) continue;
//                writer.write(currentLine + System.getProperty("line.separator"));
//            }
//            writer.close();
//            reader.close();
//            boolean successful = tempFile.renameTo(inputFile);
//            return successful;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
