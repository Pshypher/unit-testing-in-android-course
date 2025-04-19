package com.techyourchance.unittesting.questions;

public class QuestionDetails {

    private final String mId;

    private final String mTitle;

    private final String mBody;

    public QuestionDetails(String id, String title, String body) {
        mId = id;
        mTitle = title;
        mBody = body;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getBody() {
        return mBody;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof QuestionDetails)) return false;

        QuestionDetails that = (QuestionDetails) o;
        return mId.equals(that.mId) && mTitle.equals(that.mTitle) && mBody.equals(that.mBody);
    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + mTitle.hashCode();
        result = 31 * result + mBody.hashCode();
        return result;
    }
}
