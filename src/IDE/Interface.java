package IDE;

import java.io.IOException;

public class Interface {
    /**
     * 메인 화면을 출력합니다.
     */
    public static void showMainScreen(UploadFile file) {
        if (file == null) {
            System.out.print(
                    """
                    ########################################
                    1. Java File Upload
                    2. Compile
                    3. Run
                    4. Reset
                    5. Show Compile Error
                    6. Exit
                    ########################################
                    """
            );
        } else {
            System.out.printf(
                """
                ########################################
                1. Java File Upload
                2. Compile
                3. Run
                4. Reset
                5. Show Compile Error
                6. Exit
                
                [ Uploaded File ]
                %s
                ########################################
                """,
                file.fileName
            );
        }
    }

    /**
     * 업로드 화면을 출력합니다.
     */
    public static void showUploadScreen() {
        System.out.print(
            """
            ########################################
            [ 파일 업로드 ]
            main 함수가 존재하는 Java 파일을 업로드하세요.
            같은 디렉토리의 패키지 단위까지 인식 가능합니다.
            
            - 파일 전체 경로 입력
            - 파일을 끌어다 터미널에 놓기
            위 두 가지 방법으로 파일을 업로드하세요.
            ########################################
            """
        );
    }

    /**
     * 터미널을 초기화합니다.
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