package IDE;

import java.util.Scanner;

/**
 * IDE App 클래스
 */
public class App {
    private static App instance;
    private UploadFile uploadFile;
    private Controller controller;

    private App() {}

    /**
     * 싱글톤 인스턴스를 반환하는 메소드입니다.
     *
     * @return App 인스턴스
     */
    public static App getInstance() {
        if(instance == null) {
            instance = new App();
        }
        return instance;
    }

    /**
     * IDE 메인 실행 메소드
     */
    public void run() {
        try {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                Interface.showMainScreen(this.uploadFile);

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> uploadFiles(scanner);
                    case 2 -> compileFiles();
                    case 3 -> runFiles();
                    case 4 -> reset();
                    case 5 -> showCompileError();
                    case 6 -> exit();
                    default -> System.out.println("잘못 선택하셨습니다.\n다시 선택해 주세요.\n");
                }
            }
        } catch (Exception e) {
            System.out.printf("프로그램 실행에 문제가 발생했습니다.\nError: %s\n", e.getMessage());
        }
    }

    /**
     * 파일을 업로드하는 메소드입니다.
     *
     * @param scanner 입력 Scanner
     * **/
    private void uploadFiles(Scanner scanner) {
        if (this.uploadFile != null) {
            System.out.println("이미 업로드 된 파일이 존재합니다.\n파일을 삭제하고 싶으시다면 Reset을 진행해 주세요.\n");
            return;
        }

        try {
            Interface.clearScreen();
            Interface.showUploadScreen();

            scanner.nextLine();    // Scanner 버퍼 초기화
            String filePath = scanner.nextLine();

            if (!UploadFile.isFile(filePath)) {
                Interface.clearScreen();
                System.out.println("잘못된 파일 경로입니다.\n");
                return;
            }

            this.uploadFile = new UploadFile(filePath);

            Interface.clearScreen();
            System.out.println("업로드가 완료되었습니다.\n");
        } catch (Exception e) {
            System.out.printf("업로드에 문제가 발생했습니다.\nError: %s\n\n", e);
        }
    }

    /**
     * 업로드 된 파일을 컴파일합니다.
     */
    private void compileFiles() {
        try {
            if (this.uploadFile == null) {
                System.out.println("업로드 된 파일이 없습니다.\n먼저 업로드를 진행해 주세요.\n");
                return;
            }

            this.controller = new Controller();
            this.controller.compile(uploadFile);
        } catch (Exception e) {
            System.out.printf("컴파일 중 문제가 발생했습니다.\nError: %s\n\n", e);
        }
    }

    /**
     * 컴파일 된 파일을 실행합니다.
     */
    private void runFiles() {
        try {
            // ! 업로드 상태, 컴파일 상태 둘 다 확인 필요
            if (this.uploadFile == null) {
                System.out.println("업로드된 파일이 없습니다.\n먼저 파일을 업로드해 주세요.\n");
                return;
            } else if (this.controller == null) {
                System.out.println("파일이 컴파일 되지 않았습니다.\n컴파일을 진행해 주세요.\n");
                return;
            }

            this.controller.run(uploadFile);
        } catch (Exception e) {
            System.out.printf("실행 중 문제가 발생했습니다.\nError: %s\n\n", e);
        }
    }

    /**
     * IDE의 모든 상태를 초기화합니다.
     */
    private void reset() {
        try {
            Interface.clearScreen();
            this.uploadFile = null;
            this.controller = null;
            System.out.println("IDE의 모든 상태를 초기화했습니다.\n");
        } catch (Exception e) {
            System.out.printf("리셋 진행 중 문제가 발생했습니다.\nError: %s\n\n", e);
        }
    }

    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    private void showCompileError() {
        try {
            if (this.controller == null) {
                Interface.clearScreen();
                System.out.println("컴파일 기록이 존재하지 않습니다.\n");
            } else {
                Interface.clearScreen();
                this.controller.showError();
            }
        } catch (Exception e) {
            System.out.printf("컴파일 오류 메시지 출력에 문제가 발생했습니다.\nError: %s\n\n", e);
        }
    }

    /**
     * 프로그램을 종료합니다.
     */
    private void exit() {
        Interface.clearScreen();
        System.out.print("프로그램을 종료합니다.");
        System.exit(0);
    }
}