
public class IndexItem {
    public IndexItem(Integer word, Integer linenumber) {
        this.word = word;
        this.linenumber = linenumber;
    }

    public Integer getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(Integer linenumber) {
        this.linenumber = linenumber;
    }

    public Integer getWord() {
        return word;
    }

    public void setWord(Integer word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return linenumber.toString() + "(" + word + ")";
    }

    private Integer linenumber;
    private Integer word;

}
