package cn.yionr.entity;

public class Ranking {
    private long time;
    private int mine;
    private long rank;

    @Override
    public String toString() {
        return "Ranking{" +
                "time='" + time + '\'' +
                ", mine=" + mine +
                ", rank=" + rank +
                '}';
    }

    public Ranking() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}
