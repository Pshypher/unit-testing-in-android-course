package com.techyourchance.testdrivendevelopment.exercise8.contacts;

import java.util.Objects;

public class Contact {

    private final String mId;
    private final String mFullName;
    private final String mImageUrl;

    public Contact(String id, String fullName, String imageUrl) {
        mId = id;
        mFullName = fullName;
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Contact contact)) return false;
        return Objects.equals(mId, contact.mId) && Objects.equals(mFullName, contact.mFullName) && Objects.equals(mImageUrl, contact.mImageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mFullName, mImageUrl);
    }
}
