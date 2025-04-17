package com.techyourchance.testdrivendevelopment.exercise8;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.techyourchance.testdrivendevelopment.exercise8.contacts.Contact;
import com.techyourchance.testdrivendevelopment.exercise8.networking.ContactSchema;
import com.techyourchance.testdrivendevelopment.exercise8.networking.GetContactsHttpEndpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {
    // region constants
    public static final String FILTER_TERM = "filter term";
    public static final String ID = "id";
    public static final String FULL_NAME = "fullName";
    public static final String FULL_PHONE_NUMBER = "fullPhoneNumber";
    public static final String IMAGE_URL = "imageUrl";
    public static final int AGE = 35;
    // endregion constants

    // region helper fields
    @Mock
    GetContactsHttpEndpoint mGetContactsHttpEndpointMock;
    @Mock
    FetchContactsUseCase.Observer mFetchContactsUseCaseObserverMock1;
    @Mock
    FetchContactsUseCase.Observer mFetchContactsUseCaseObserverMock2;
    @Captor ArgumentCaptor<List<Contact>> mAcListContact;
    // endregion helper fields

    FetchContactsUseCase SUT;

    @Before
    public void setup() {
        SUT = new FetchContactsUseCase(mGetContactsHttpEndpointMock);
        success();
    }

    @Test
    public void getContacts_passesCorrectFilterTermToEndpoint() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.getContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mGetContactsHttpEndpointMock).getContacts(ac.capture(), any(GetContactsHttpEndpoint.Callback.class));
        assertThat(ac.getValue(), is(FILTER_TERM));
    }

    @Test
    public void getContacts_success_allObserversNotifiedWithData() {
        // Arrange
        // Act
        SUT.registerListener(mFetchContactsUseCaseObserverMock1);
        SUT.registerListener(mFetchContactsUseCaseObserverMock2);
        SUT.getContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mFetchContactsUseCaseObserverMock1).notifySuccess(mAcListContact.capture());
        verify(mFetchContactsUseCaseObserverMock2).notifySuccess(mAcListContact.capture());
        List<List<Contact>> contacts = mAcListContact.getAllValues();
        assertThat(contacts.get(0), is(getContacts()));
        assertThat(contacts.get(1), is(getContacts()));
    }

    @Test
    public void getContacts_success_unregisteredObserverNotNotified() {
        // Arrange
        // Act
        SUT.registerListener(mFetchContactsUseCaseObserverMock1);
        SUT.registerListener(mFetchContactsUseCaseObserverMock2);
        SUT.unregisterListener(mFetchContactsUseCaseObserverMock2);
        SUT.getContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mFetchContactsUseCaseObserverMock1).notifySuccess(mAcListContact.capture());
        verifyNoMoreInteractions(mFetchContactsUseCaseObserverMock2);
        assertThat(mAcListContact.getValue(), is(getContacts()));
    }

    @Test
    public void getContacts_generalError_allObserversNotified() {
        // Arrange
        generalError();
        // Act
        SUT.registerListener(mFetchContactsUseCaseObserverMock1);
        SUT.registerListener(mFetchContactsUseCaseObserverMock2);
        SUT.getContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mFetchContactsUseCaseObserverMock1).notifyFailure();
        verify(mFetchContactsUseCaseObserverMock2).notifyFailure();
    }

    @Test
    public void getContacts_networkError_allObserversNotified() {
        // Arrange
        networkError();
        // Act
        SUT.registerListener(mFetchContactsUseCaseObserverMock1);
        SUT.registerListener(mFetchContactsUseCaseObserverMock2);
        SUT.getContactsAndNotify(FILTER_TERM);
        // Assert
        verify(mFetchContactsUseCaseObserverMock1).notifyFailure();
        verify(mFetchContactsUseCaseObserverMock2).notifyFailure();
    }

    // region helper methods
    private List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGE_URL));
        return contacts;
    }

    private void success() {
        doAnswer(invocation -> {
            GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
            List<ContactSchema> contactSchemas = new ArrayList<>();
            contactSchemas.add(new ContactSchema(ID, FULL_NAME, FULL_PHONE_NUMBER, IMAGE_URL, AGE));
            callback.onGetContactsSucceeded(contactSchemas);
            return null;
        }).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void generalError() {
        doAnswer(
                invocation -> {
                    GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
                    callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.GENERAL_ERROR);
                    return null;
                }
        ).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }

    private void networkError() {
        doAnswer(
                invocation -> {
                    GetContactsHttpEndpoint.Callback callback = invocation.getArgument(1);
                    callback.onGetContactsFailed(GetContactsHttpEndpoint.FailReason.NETWORK_ERROR);
                    return null;
                }
        ).when(mGetContactsHttpEndpointMock).getContacts(anyString(), any(GetContactsHttpEndpoint.Callback.class));
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}