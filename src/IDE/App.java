package IDE;

import java.util.Scanner;

public class App {
    /**
     * IDE 메인 실행 메소드
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Interface.showScreen();

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 -> uploadFiles(scanner);
                case 2 -> compileFiles();
                case 3 -> runFiles();
                case 4 -> reset();
                case 5 -> showCompileError();
                case 6 -> exit();
                default -> System.out.println("잘못 선택하셨습니다.\n다시 선택해 주세요.");
            }
        }
    }

    /**
     * 파일을 업로드하는 메소드입니다.
     * <p>
     * - 사용자가 파일을 터미널에 끌어다 놓으면 자동으로 목록에 추가됩니다.<p>
     *       -> ".java"를 인식하면 입력 종료
     * - 파일을 끌어다 놓으면 터미널을 초기화합니다.<p>
     *
     * @param scanner 입력 Scanner
     * **/
    private void uploadFiles(Scanner scanner) {
        Interface.clearScreen();
    }

    /**
     * 업로드 되어 있는 파일을 컴파일합니다.
     * <p>
     * - 파일 업로드 확인
     * - ProcessBuilder 컴파일 명령어 실행
     *       1. main 함수 파일 컴파일         javac main.java
     *       2. 패키지단 디렉토리 파일 컴파일    javac *.java
     * - 컴파일 결과 저장
     *       : 컴파일 여부
     *       : 오류 발생 여부
     *       : 외부 터미널 출력 결과
     */
    private void compileFiles() {}

    /**
     * 컴파일 된 파일을 실행합니다.
     * <p>
     * - 파일 컴파일 확인
     * - ProcessBuilder 실행 명령어 실행
     */
    private void runFiles() {}

    /**
     * 업로드 된 파일을 삭제합니다.
     */
    private void reset() {}

    /**
     * 컴파일 시 발생한 오류를 출력합니다.
     */
    private void showCompileError() {}

    /**
     * 프로그램을 종료합니다.
     */
    private void exit() {
        System.out.print("프로그램을 종료합니다.");
        System.exit(0);
    }
}