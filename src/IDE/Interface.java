package IDE;

import java.io.IOException;

public class Interface {
    public static void showScreen() {
        /**
         * 메인 메뉴를 출력합니다.
         */
        System.out.println(
            """
            ####################
            1. Java File Upload
            2. Compile
            3. Run
            4. Reset
            5. Compile Error File
            6. Exit
            ####################
            """
        );
    }

    /**
     * 터미널을 초기화합니다.
     * <p>
     * ! 초기화 되지 않는 문제 해결 필요
     */
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException _) {}
    }
}