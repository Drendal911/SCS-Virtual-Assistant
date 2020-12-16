package Utility;

public class CardViewItem {
    private final int mImageResource;
    private final String mText1;
    private final String mText2;

    public CardViewItem(int mImageResource, String mText1, String mText2){
        this.mImageResource = mImageResource;
        this.mText1 = mText1;
        this.mText2 = mText2;
    }

    public int getImageResource() {
        return mImageResource;
    }

    public String getText1() {
        return mText1;
    }

    public String getText2() {
        return mText2;
    }

}
