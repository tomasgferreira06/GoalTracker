package com.example.gps_g12.goalTracker.model.data;

import javafx.beans.property.BooleanProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class Data {

    User user;

    public Data(){
        this.user = new User();
        //loadTasksFromFile();
    }

    public ArrayList<Task> getUserTasks(){return user.getTasks();}

    public void addNewTask(Task newTask){
        user.addNewTask(newTask);
        saveNewTaskOnFile(newTask);
    }


    public void saveNewTaskOnFile(Task newTask) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
            ArrayList<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            // Adicione os detalhes da nova tarefa à lista
            String newTaskLine = newTask.getId() + "," + newTask.getName() + "," + newTask.getDate().getDay() + "/" + newTask.getDate().getMonth() + "/" + newTask.getDate().getYear() + "," + newTask.getHour().getHours() + ":" + newTask.getHour().getMinutes() + "," + newTask.getFrequency() + "," + newTask.getPriority() + "," + newTask.getTimesPerDay() + "," + newTask.getReminder() + "," + newTask.isCompleted() + "," + newTask.getParentId();
            lines.add(0, newTaskLine);

            // Reescreva todo o conteúdo do arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt", false))) {
                for (String fileLine : lines) {
                    writer.println(fileLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente para a sua aplicação
        }
    }


    public void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] taskDetails = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if(taskDetails.length > 1 /*&& !taskDetails[taskDetails.length -1].equals("true")*/){
                    try {
                        // Crie uma nova instância de Task usando os detalhes do arquivo

                        Task loadedTask = new Task(
                                Integer.parseInt(taskDetails[0]),
                                taskDetails[1],  // Nome
                                new Date(Integer.parseInt(taskDetails[2].split("/")[0]), Integer.parseInt(taskDetails[2].split("/")[1]), Integer.parseInt(taskDetails[2].split("/")[2])),  // Data
                                new Hour(Integer.parseInt(taskDetails[3].split(":")[0]), Integer.parseInt(taskDetails[3].split(":")[1])),  // Hora
                                taskDetails[4],  // Frequência
                                taskDetails[5],  // Prioridade
                                Integer.parseInt(taskDetails[6]),  // Vezes por dia
                                Boolean.parseBoolean(taskDetails[7]),  // Lembrete
                                Boolean.parseBoolean(taskDetails[8]),
                                Integer.parseInt(taskDetails[9])
                        );

                        //Remove completed task from file so when the program loads doesnt appear in the completed ones
                        if(loadedTask.isCompleted()){
                            deleteTaskFromFile(loadedTask, true);
                            user.setStreak(getStreakFromFile() + 1);
                            streakOnFile(user.getStreak());
                            continue;
                        }

                        // Adicione a tarefa carregada à lista de tarefas do usuário

                        int loadedDayFromFile = Integer.parseInt(taskDetails[2].split("/")[0]);
                        int loadedMonthFromFile = Integer.parseInt(taskDetails[2].split("/")[1]);
                        int loadedYearFromFile = Integer.parseInt(taskDetails[2].split("/")[2]);

                        LocalDate systemDate = LocalDate.now();

                        int systemDay = systemDate.getDayOfMonth();
                        int systemMonth = systemDate.getMonthValue();
                        int systemYear = systemDate.getYear();

                        if(systemDay == loadedDayFromFile  && systemMonth == loadedMonthFromFile && systemYear == loadedYearFromFile){ //today
                            loadedTask.setType("Today");
                            user.addToTodayTasks(loadedTask);
                        }else if(systemDay + 1 == loadedDayFromFile && systemMonth == loadedMonthFromFile && systemYear == loadedYearFromFile){ //tomorrow
                            loadedTask.setType("Tomorrow");
                            user.addToTomorrowTasks(loadedTask);
                        }else if(loadedDayFromFile == 0 && loadedMonthFromFile == 0 && loadedYearFromFile == 0){ //no spec date
                            loadedTask.setType("NoSpecDate");
                            user.addToNoSpecDateTasks(loadedTask);
                        }else{ //random day
                            loadedTask.setType("Random");
                            user.addNewTask(loadedTask);
                        }

                        //user.addNewTask(loadedTask);
                    } catch (NumberFormatException e) {
                        System.out.println("Erro ao converter números na linha: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Trate a exceção apropriadamente para a sua aplicação
        }
    }

    public void markTaskAsCompletedInFile(Task completedTask) {
        try {
            // Ler todas as linhas do arquivo
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Procurar pela linha correspondente à tarefa concluída e atualizar
            for (int i = 0; i < lines.size(); i++) {
                String[] taskDetails = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String taskId = taskDetails[0];

                // Verificar se a linha corresponde à tarefa concluída
                if (Integer.parseInt(taskId) == completedTask.getId()) {
                    // Atualizar a última string para "true"
                    taskDetails[taskDetails.length - 2] = "true";
                    lines.set(i, String.join(",", taskDetails));
                    break;
                }

            }

            // Escrever as linhas atualizadas de volta no arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt", false))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente para a sua aplicação
        }
    }

    public void markTaskAsUnCompletedInFile(Task completedTask) {
        try {
            // Ler todas as linhas do arquivo
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Procurar pela linha correspondente à tarefa concluída e atualizar
            for (int i = 0; i < lines.size(); i++) {
                String[] taskDetails = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String taskId = taskDetails[0];

                // Verificar se a linha corresponde à tarefa concluída
                if (Integer.parseInt(taskId) == completedTask.getId()) {
                    // Atualizar a última string para "true"
                    taskDetails[taskDetails.length - 2] = "false";
                    lines.set(i, String.join(",", taskDetails));
                    break;
                }

            }

            // Escrever as linhas atualizadas de volta no arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt", false))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente para a sua aplicação
        }
    }

    public void editTaskOnFile(Task taskToEdit) {
        try {
            // Ler todas as linhas do arquivo
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            // Procurar pela linha correspondente à tarefa a ser removida e excluí-la
            for (int i = 0; i < lines.size(); i++) {
                String[] taskDetails = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String taskId = taskDetails[0];

                // Verificar se a linha corresponde à tarefa a ser removida
                if (Integer.parseInt(taskId) == taskToEdit.getId()) {
                    String taskToEditLine = taskToEdit.getId() + "," + taskToEdit.getName() + "," + taskToEdit.getDate().getDay() + "/" + taskToEdit.getDate().getMonth() + "/" + taskToEdit.getDate().getYear() + "," + taskToEdit.getHour().getHours() + ":" + taskToEdit.getHour().getMinutes() + "," + taskToEdit.getFrequency() + "," + taskToEdit.getPriority() + "," + taskToEdit.getTimesPerDay() + "," + taskToEdit.getReminder() + "," + taskToEdit.isCompleted() + "," + taskToEdit.getParentId();
                    lines.set(i, taskToEditLine);
                    break;
                }
            }

            // Escrever as linhas atualizadas de volta no arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt", false))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente para a sua aplicação
        }
    }

    public int getStreakFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/streak.txt"))) {
            String line;
            int streak = 0;
            while ((line = reader.readLine()) != null) {
                String[] taskDetails = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                user.setStreak(Integer.parseInt(taskDetails[0]));
                streak = user.getStreak();
            }
            return streak;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing streak value: " + e.getMessage());
        }

        // Se houver algum erro, retorne um valor padrão (0)
        return 0;
    }

    public void streakOnFile(int newStreak) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/streak.txt", false))) {
            // Escreva o novo streak na primeira linha
            writer.write(String.valueOf(newStreak));
            writer.newLine();

            // Adicione o restante do conteúdo original (se houver)
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/streak.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Integer> getChildrensToRemove(int parentId){

        ArrayList<Integer> auxArrayList = new ArrayList<>();

        /*Iterator<Task> iterator = user.getTodayTasks().iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.getParentId() == parentId) {
                auxArrayList.add(t.getId());
                iterator.remove();
            }
        }*/

        Iterator<Task> iterator = user.getTomorrowTasks().iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.getParentId() == parentId) {
                auxArrayList.add(t.getId());
                iterator.remove();
            }
        }

        iterator = user.getNoSpecDateTasks().iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.getParentId() == parentId) {
                auxArrayList.add(t.getId());
                iterator.remove();
            }
        }

        iterator = user.getTasks().iterator();
        while (iterator.hasNext()) {
            Task t = iterator.next();
            if (t.getParentId() == parentId) {
                auxArrayList.add(t.getId());
                iterator.remove();
            }
        }

        return auxArrayList;
    }

    public void deleteTaskFromFile(Task taskToDelete, boolean deleteFirstFlag) {
        try {
            // Ler todas as linhas do arquivo
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            ArrayList<Integer> childrenToRemove = new ArrayList<>();
            childrenToRemove = getChildrensToRemove(taskToDelete.getParentId());

            // Procurar pela linha correspondente à tarefa a ser removida e excluí-la
            for (int i = lines.size() - 1; i >= 0; i--) {
                String[] taskDetails = lines.get(i).split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String taskId = taskDetails[0];

                // Verificar se a linha corresponde à tarefa a ser removidas
                if(deleteFirstFlag){
                    if(Integer.parseInt(taskId) == taskToDelete.getParentId() || Integer.parseInt(taskId) == taskToDelete.getId()){
                        lines.remove(i);

                        getUserTodayTasks().removeIf(task -> task.getId() == Integer.parseInt(taskId));

                        continue;
                    }

                    /*if (Integer.parseInt(taskId) == taskToDelete.getId()) {
                        lines.remove(i);
                        continue;
                    }*/
                }

                for (Integer integer : childrenToRemove) {
                    if (integer == Integer.parseInt(taskId)) {
                        lines.remove(i);
                        setUserStreak(0);
                        streakOnFile(0);
                    }
                }
            }


            // Escrever as linhas atualizadas de volta no arquivo
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt", false))) {
                for (String line : lines) {
                    writer.println(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Trate a exceção apropriadamente para a sua aplicação
        }
    }

    public void addTaskToCompleted(Task completedTask){
        user.addTaskToCompleted(completedTask);
        markTaskAsCompletedInFile(completedTask);
    }

    public void addToOrderedTasks(Task newOrderedTask){
        user.addToOrderedTasks(newOrderedTask);
    }
    public void addToTodayTasks(Task newTodayTask){
        user.addToTodayTasks(newTodayTask);
        saveNewTaskOnFile(newTodayTask);
    }
    public void addToTomorrowTasks(Task newTomorrowTask){
        user.addToTomorrowTasks(newTomorrowTask);
        saveNewTaskOnFile(newTomorrowTask);
    }
    public void addToNoSpecDateTasks(Task newNoSpecDateTask){
        user.addToNoSpecDateTasks(newNoSpecDateTask);
        saveNewTaskOnFile(newNoSpecDateTask);
    }
    public ArrayList<Task> getCompletedTasks(){return user.getCompletedTasks();}
    public ArrayList<Task> getUserOrderedTasks(){return user.getOrderedTasks();}
    public ArrayList<Task> getUserTodayTasks(){return user.getTodayTasks();}
    public ArrayList<Task> getUserTomorrowTasks(){return user.getTomorrowTasks();}
    public ArrayList<Task> getUserNoSpecDateTasks(){return user.getNoSpecDateTasks();}

    public int getStreak() {
        return user.getStreak();
    }

    public void setUserStreak(int newStreak){
        user.setStreak(newStreak);
    }


}
