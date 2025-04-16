package com.techyourchance.testdrivendevelopment.exercise8;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

public class FetchContactsUseCase {

    public interface Observer {
        void notifySuccess(List<Contact> contacts);

        void notifyFailure();
    }

    private final GetContactsHttpEndpoint mGetContactsHttpEndpoint;
    private final List<Observer> mObservers;

    public FetchContactsUseCase(GetContactsHttpEndpoint getContactsHttpEndpoint) {
        mGetContactsHttpEndpoint = getContactsHttpEndpoint;
        mObservers = new ArrayList<>();
    }

    public void registerListener(Observer observer) {
        mObservers.add(observer);
    }

    public void unregisterListener(Observer observer) {
        mObservers.remove(observer);
    }

    public void getContactsAndNotify(String filterTerm) {
        mGetContactsHttpEndpoint.getContacts(filterTerm, new GetContactsHttpEndpoint.Callback() {

            @Override
            public void onGetContactsSucceeded(List<ContactSchema> contacts) {
                for (Observer observer : mObservers) {
                    observer.notifySuccess(contactsFromSchema(contacts));
                }
            }

            @Override
            public void onGetContactsFailed(GetContactsHttpEndpoint.FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR, NETWORK_ERROR -> {
                        for (Observer observer : mObservers) {
                            observer.notifyFailure();
                        }
                    }
                }
            }
        });
    }

    private List<Contact> contactsFromSchema(List<ContactSchema> contacts) {
        List<Contact> output = new ArrayList<>();
        for (ContactSchema contact : contacts) {
            output.add(new Contact(contact.getId(), contact.getFullName(), contact.getImageUrl()));
        }

        return output;
    }
}
