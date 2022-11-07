/* 
<해결해야 할 것>
1. readline()메서드가 왜 WARING을 부르는지 이해하기 -> 일단 Pass
2. IllegalArgumentException이 발생했을 때, 프로그램을 종료할 수 있도록 하기
 */
package baseball;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.List;

public class Application {
    // 0. 게임의 시작을 분리한다.
    public static boolean gameStart() throws IllegalArgumentException {
        // 1. 상대방(컴퓨터)의 숫자를 설정한다.
        List<Integer> computer = new ArrayList<>();
        computer = getNumberOfComputer(computer);
        System.out.println("computer = " + computer); // 테스트 출력

        // 2. 사용자에게서 숫자를 입력받는다.
        List<Integer> user = new ArrayList<>();

        // 3. 사용자의 입력값에 따라 Ball Count를 출력한다.
        String ballCount = "";

        // 3-1 : 재입력이 필요하면 true를 반환하고, 그렇지 않으면 false를 반환한다.
        while (isNeedReEnter(ballCount)) {
            // 3-2 : user의 정보를 얻어와 String으로 받고, List로 변환한다.
            user = getNumberOfUserForList(user);

            // 3-3 : computer와 user을 비교하여 BallCount를 반환한다.
            ballCount = getBallCount(computer, user); // ex) 1볼 1스트라이크
            UI.printBallCount(ballCount);
        }

        // 4. 3개의 숫자를 모두 맞힌 경우, 게임 재시작 안내 메세지 출력 및 1 또는 2의 값을 입력받도록 한다.
        UI.printAnswerMsg();
        int restartOrExit = Integer.parseInt(Console.readLine());
        boolean restartOrNot = setRestartOrNot(restartOrExit);
        return restartOrNot;

    }

    // 1. 상대방(컴퓨터)의 숫자를 설정한다.
    private static List<Integer> getNumberOfComputer(List<Integer> computer) {
        while (computer.size() < 3) {
            int randomNumber = Randoms.pickNumberInRange(1, 9);
            if (!computer.contains(randomNumber)) {
                computer.add(randomNumber);
            }
        }
        return computer;
    }

    // 2-1 : 사용자의 입력값을 검증한다
    private static boolean isValidStringInputOfUser(String stringInputOfUser) throws IllegalArgumentException {

        int num = 0;

        // 2-1-1. 정수가 아닌 값을 입력했을 시 오류를 발생시킨다.
        try {
            num = Integer.parseInt(stringInputOfUser);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("정수로 변환할 수 없는 문자입니다.");
        }

        // 2-1-2. 세 자리의 수가 아닌 경우 오류를 발생시킨다.
        if (num > 1000 || num < 100) {
            throw new IllegalArgumentException("세 자리의 수가 아닙니다.");
        }

        // 2-1-3. 서로 다른 세 자리의 수가 아닌 경우 오류를 발생시킨다.
        int hundreds = num / 100;
        int tens = (num % 100) / 10;
        int ones = num % 10;

        if (hundreds == tens || hundreds == ones || tens == ones) {
            throw new IllegalArgumentException("서로 다른 세 자리의 수가 아닙니다.");
        }

        return true;
    }

    // 2-2 : 사용자의 입력값을 List로 바꾼다.
    private static List<Integer> stringToList(String numOfUserString) {
        List<Integer> user = new ArrayList<>();
        int numOfUserInteger = Integer.parseInt(numOfUserString);

        int hundreds = numOfUserInteger / 100;
        int tens = (numOfUserInteger % 100) / 10;
        int ones = numOfUserInteger % 10;

        user.add(hundreds);
        user.add(tens);
        user.add(ones);

        return user;
    }

    // 3-1 : 재입력이 필요하면 true를 반환하고, 그렇지 않으면 false를 반환한다.
    private static boolean isNeedReEnter(String ballCount) {
        // 3-1-1. 3S가 나오지 못하면 true를 반환하고, 그렇지 않으면 false를 반환한다.
        if (!ballCount.equals("3스트라이크")) {
            return true;
        } else {
            return false;
        }
//        return !ballCount.equals("3스트라이크");
    }

    // 3-2 : user의 정보를 얻어와 String으로 받고, List로 변환한다.
    private static List<Integer> getNumberOfUserForList(List<Integer> user) throws IllegalArgumentException {
        user.clear();
        UI.printInputNumOfUserMsg();

        // 3-2-1 : user의 Number을 String의 형태로 얻어온다.
        String numberOfUserString;
        try {
            numberOfUserString = getNumberOfUserString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }

        // 3-2-2 : String의 형태를 List의 형태로 바꾼다.
        user = getNumberOfUser(user, numberOfUserString);
        return user;
    }

    // 3-2-1 : user의 Number을 String의 형태로 얻어온다.
    public static String getNumberOfUserString() {
        String numOfUserString = Console.readLine();
        return numOfUserString;
    }

    // 3-2-2 : String의 형태를 List의 형태로 바꾼다.
    private static List<Integer> getNumberOfUser(List<Integer> user, String numberOfUserString) throws
            IllegalArgumentException {

        // 3-2-2-1 : 사용자의 입력값을 검증한다.
        try {
            isValidStringInputOfUser(numberOfUserString);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return user;
        }

        // 3-2-2-2 : 사용자의 입력값을 List로 바꾼다.
        user = stringToList(numberOfUserString);
        return user;
    }

    // 3-3 : computer와 user을 비교하여 BallCount를 반환한다.
    private static String getBallCount(List<Integer> computer, List<Integer> user) {
        // 3-3-1. 스트라이크 count하기
        int countOfStrike = howMuchStrike(computer, user);

        // 3-3-2. 볼 conut하기
        int countOfBall = howMuchBall(computer, user);

        // 3-3-3. 두 문자열을 합치기.
        String totalCount = "";
        if (countOfBall == 0 && countOfStrike != 0) {
            totalCount = countOfStrike + "스트라이크";
        } else if (countOfBall != 0 && countOfStrike == 0) {
            totalCount = countOfBall + "볼";
        } else if (countOfBall == 0 && countOfStrike == 0) {
            totalCount = "낫싱";
        } else if (countOfBall > 0 && countOfStrike > 0) {
            totalCount = countOfBall + "볼 " + countOfStrike + "스트라이크";
        } else {
            totalCount = "비정상";
        }

        return totalCount;
    }

    // 3-3-1 : strike count하기
    private static int howMuchStrike(List<Integer> computer, List<Integer> user) {
        int strikeCount = 0;

        for (int i = 0; i < computer.size(); i++) {
            if (user.isEmpty()) {
                return -1;
            } else if (computer.get(i) == user.get(i)) {
                strikeCount++;
            }
        }
        return strikeCount;
    }

    // 3-3-2 : ball count하기
    private static int howMuchBall(List<Integer> computer, List<Integer> user) {
        int ballCount = 0;

        if (user.isEmpty()) {
            return -1;
        }

        if (computer.get(0) == user.get(1) || computer.get(0) == user.get(2)) {
            ballCount++;
        }
        if (computer.get(1) == user.get(0) || computer.get(1) == user.get(2)) {
            ballCount++;
        }
        if (computer.get(2) == user.get(0) || computer.get(2) == user.get(1)) {
            ballCount++;
        }

        return ballCount;
    }

    // 4-2 : Restart할지, 그렇지 않을지 정한다.
    private static boolean setRestartOrNot(int restartOrExit) {
        if (restartOrExit == 1) {
            return true;
        } else if (restartOrExit == 2) {
            return false;
        } else {
            throw new IllegalArgumentException("비정상적인 값입니다.");
        }
    }

    // 4-3-1 : 재시작하기 위한 준비
    private static void preSettingToRestartGame(List<Integer> computer, List<Integer> user) {
        computer.clear();
        computer = getNumberOfComputer(computer);
        System.out.println("computer = " + computer); // 테스트 출력 : 여기까지 이상없이 되는 것 확인했음
        user.clear();
    }

    // 4-3-2 : 재시작하기
    private static void restartGame(List<Integer> user) {
        user = getNumberOfUserForList(user);
    }

    public static void main(String[] args) {
        boolean repeatState = true;

        UI.printGameStartMsg();
        while (repeatState) {
            repeatState = gameStart();
        }
    }
}