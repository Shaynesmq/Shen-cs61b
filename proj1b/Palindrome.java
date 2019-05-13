public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> wordDeque = new ArrayDeque<>();

        /* @source Got help from internet, return array of characters in word */
        char[] chars = word.toCharArray();
        for (char c : chars) {
            wordDeque.addLast(c);
        }
        return wordDeque;
    }

    private boolean isPalindrome(Deque<Character> wordDeque, boolean isForNow) {
        if (!isForNow) {
            return false;
        }

        if (wordDeque.size() <= 1 && isForNow) {
            return true;
        }

        char first = wordDeque.removeFirst();
        char last = wordDeque.removeLast();
        if (first != last) {
            isForNow = false;
        }
        return isPalindrome(wordDeque, isForNow);
    }

    public boolean isPalindrome(String word) {
        /*boolean flag = true;
        int wordLength = word.length();

        *//* Any word of length 1 or 0 is a palindrome. *//*
        if (wordLength > 1) {
            Deque<Character> wordDeque = wordToDeque(word);
            for (int i = 0; i < wordLength / 2; i++) {
                char front = wordDeque.removeFirst();
                char back = wordDeque.removeLast();
                if (front != back) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;*/

        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindrome(wordDeque, true);
    }

    private boolean isPalindrome(Deque<Character> wordDeque, boolean isForNow, CharacterComparator cc) {
        if (!isForNow) {
            return false;
        }

        if (wordDeque.size() <= 1 && isForNow) {
            return true;
        }

        char first = wordDeque.removeFirst();
        char last = wordDeque.removeLast();
        if (!cc.equalChars(first, last)) {
            isForNow = false;
        }
        return isPalindrome(wordDeque, isForNow, cc);
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> wordDeque = wordToDeque(word);
        return isPalindrome(wordDeque, true, cc);
    }
}
