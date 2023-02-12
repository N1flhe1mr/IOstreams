import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    static StringBuilder logs = new StringBuilder();
    static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        createDir("C:/Games/src");
        createDir("C:/Games/res");
        createDir("C:/Games/savegames");
        createDir("C:/Games/temp");
        createDir("C:/Games/src/main");
        createDir("C:/Games/src/test");
        createDir("C:/Games/res/drawbles");
        createDir("C:/Games/res/vectors");
        createDir("C:/Games/res/icons");
        createFile("C:/Games/src/main/Main.java");
        createFile("C:/Games/src/main/Utils.java");
        createFile("C:/Games/temp/temp.txt");
        writeLogs();
        GameProgress gameProgress1 = new GameProgress(100, 2, 3, 3.8);
        GameProgress gameProgress2 = new GameProgress(100, 2, 6, 6.4);
        GameProgress gameProgress3 = new GameProgress(40, 2, 8, 12.3);
        saveGame("C:/Games/savegames/save1.dat", gameProgress1);
        saveGame("C:/Games/savegames/save2.dat", gameProgress2);
        saveGame("C:/Games/savegames/save3.dat", gameProgress3);
        list.add("C:/Games/savegames/save1.dat");
        list.add("C:/Games/savegames/save2.dat");
        list.add("C:/Games/savegames/save3.dat");
        zipFiles("C:/Games/savegames/savegames.zip", list);
        openZip("C:/Games/savegames/savegames.zip");
        openProgress("C:/Games/savegames/save1.dat");
        openProgress("C:/Games/savegames/save2.dat");
        openProgress("C:/Games/savegames/save3.dat");
    }

    public static void createDir(String dir) {
        File newDir = new File(dir);
        if (newDir.mkdir()) {
            logs.append("Time:");
            logs.append(LocalDateTime.now());
            logs.append(" Directory name:");
            logs.append(newDir.getName());
            logs.append(" Directory path:");
            logs.append(newDir.getPath());
            logs.append(" Directory size:");
            logs.append(newDir.length());
            logs.append('\n');
        } else {
            System.out.println("Directory don't created");
        }
    }

    public static void createFile(String file) throws IOException {
        File newFile = new File(file);
        try {
            if (newFile.createNewFile()) {
                logs.append("Time:");
                logs.append(LocalDateTime.now());
                logs.append(" File name:");
                logs.append(newFile.getName());
                logs.append(" File path:");
                logs.append(newFile.getPath());
                logs.append(" File size:");
                logs.append(newFile.length());
                logs.append('\n');
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeLogs() {
        try (FileWriter fw = new FileWriter("C:/Games/temp/temp.txt", false)) {
            fw.write(logs.toString());
            fw.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipPath, List list) {
        try (ZipOutputStream zout = new ZipOutputStream(new
                FileOutputStream(zipPath))) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String save = iterator.next();
                FileInputStream fis = new FileInputStream(save);
                ZipEntry entry = new ZipEntry(save);
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                fis.close();
                deleteFile(save);
            }
            zout.closeEntry();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int i = zin.read(); i != -1; i = zin.read()) {
                    fout.write(i);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        deleteFile(zipPath);
    }

    public static void openProgress(String save) {
        GameProgress gameProgress = null;
        try (FileInputStream fin = new FileInputStream(save);
             ObjectInputStream oin = new ObjectInputStream(fin)) {
            gameProgress = (GameProgress) oin.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(gameProgress.toString());
    }

    public static void deleteFile(String file) {
        File newFile = new File(file);
        if (newFile.delete()) ;
    }
}