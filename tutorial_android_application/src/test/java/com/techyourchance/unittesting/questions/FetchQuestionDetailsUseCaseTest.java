package com.techyourchance.unittesting.questions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.techyourchance.unittesting.networking.questions.FetchQuestionDetailsEndpoint;
import com.techyourchance.unittesting.networking.questions.QuestionSchema;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FetchQuestionDetailsUseCaseTest {

    // region constants
    public static final String TITLE = "title";
    public static final String ID = "id";
    public static final String BODY = "body";
    // endregion constants

    // region helper fields
    @Mock
    FetchQuestionDetailsEndpoint mFetchQuestionDetailsEndpointMock;
    @Mock
    FetchQuestionDetailsUseCase.Listener mListenerMock1;
    @Mock
    FetchQuestionDetailsUseCase.Listener mListenerMock2;
    // endregion helper fields

    FetchQuestionDetailsUseCase SUT;

    @Before
    public void setup() {
        SUT = new FetchQuestionDetailsUseCase(mFetchQuestionDetailsEndpointMock);

    }

    @Test
    public void fetchQuestionDetailsAndNotify_correctQuestionIdPassedInEndpoint() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.fetchQuestionDetailsAndNotify(ID);
        // Assert
        verify(mFetchQuestionDetailsEndpointMock).fetchQuestionDetails(ac.capture(), any(FetchQuestionDetailsEndpoint.Listener.class));
        assertThat(ac.getValue(), is(ID));
    }

    @Test
    public void fetchQuestionDetailsAndNotify_success_notifyObserversWithCorrectData() {
        // Arrange
        success();
        ArgumentCaptor<QuestionDetails> ac = ArgumentCaptor.forClass(QuestionDetails.class);
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchQuestionDetailsAndNotify(ID);
        // Assert
        verify(mListenerMock1).onQuestionDetailsFetched(ac.capture());
        verify(mListenerMock2).onQuestionDetailsFetched(ac.capture());
        List<QuestionDetails> questions = ac.getAllValues();
        assertThat(questions.get(0), is(getQuestion()));
        assertThat(questions.get(1), is(getQuestion()));
    }

    @Test
    public void fetchQuestionDetailsAndNotify_success_unsubscribedObserverNotNotified() {
        // Arrange
        success();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.unregisterListener(mListenerMock2);
        SUT.fetchQuestionDetailsAndNotify(ID);
        // Assert
        verify(mListenerMock1).onQuestionDetailsFetched(any(QuestionDetails.class));
        verifyNoMoreInteractions(mListenerMock2);
    }

    @Test
    public void fetchQuestionDetailsAndNotify_failure_notifyObservers() {
        // Arrange
        failure();
        // Act
        SUT.registerListener(mListenerMock1);
        SUT.registerListener(mListenerMock2);
        SUT.fetchQuestionDetailsAndNotify(ID);
        // Assert
        verify(mListenerMock1).onQuestionDetailsFetchFailed();
        verify(mListenerMock2).onQuestionDetailsFetchFailed();
    }

    // region helper methods
    private QuestionSchema getQuestionSchema() {
        return new QuestionSchema(TITLE, ID, BODY);
    }

    private QuestionDetails getQuestion() {
        return new QuestionDetails(ID, TITLE, BODY);
    }

    private void success() {
        doAnswer(invocation -> {
                    FetchQuestionDetailsEndpoint.Listener listener = invocation.getArgument(1);
                    listener.onQuestionDetailsFetched(getQuestionSchema());
                    return null;
                }
        ).when(mFetchQuestionDetailsEndpointMock).fetchQuestionDetails(anyString(), any(FetchQuestionDetailsEndpoint.Listener.class));
    }

    private void failure() {
        doAnswer( invocation -> {
            FetchQuestionDetailsEndpoint.Listener listener = invocation.getArgument(1);
            listener.onQuestionDetailsFetchFailed();
            return null;
        }).when(mFetchQuestionDetailsEndpointMock).fetchQuestionDetails(anyString(), any(FetchQuestionDetailsEndpoint.Listener.class));
    }
    // endregion helper methods

    // region helper classes

    // endregion helper classes
}