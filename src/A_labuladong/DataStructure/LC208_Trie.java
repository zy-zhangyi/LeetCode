package A_labuladong.DataStructure;

public class LC208_Trie {
    private LC208_Trie[] children;
    private boolean isEnd;

    public LC208_Trie()
    {
        children = new LC208_Trie[26];
        isEnd = false;
    }

    public void insert(String word)
    {
        LC208_Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            char ch = word.charAt(i);
            int index = ch - 'a';
            if (node.children[index] == null) node.children[index] = new LC208_Trie();
            node = node.children[index];
        }
        node.isEnd = true;
    }

    public boolean search(String word)
    {
        LC208_Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            node = node.children[word.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return isEnd;
    }

    public boolean prefix(String word)
    {
        LC208_Trie node = this;
        for (int i = 0; i < word.length(); i++)
        {
            node = node.children[word.charAt(i) - 'a'];
            if (node == null) return false;
        }
        return true;
    }
}
