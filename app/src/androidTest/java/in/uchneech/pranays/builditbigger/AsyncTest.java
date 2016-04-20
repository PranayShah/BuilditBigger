package in.uchneech.pranays.builditbigger;

import android.os.AsyncTask;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import in.uchneech.pranays.builditbigger.backend.myApi.MyApi;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AsyncTest{
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class);
    @Test
    public void test () throws Throwable {
        // create  a signal to let us know when our task is done.
        //final CountDownLatch signal = new CountDownLatch(1);
        // Execute the async task on the UI thread! THIS IS KEY!
        mActivityRule.getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

                //new EndpointsAsyncTask().execute(mActivityRule.getActivity());
                new AsyncTask<Void, Void, String> () {

                    private static final String JOKE_CONSTANT = "joke";
                    private MyApi myApiService = null;
                    @Override
                    protected String doInBackground(Void... params) {
                        if(myApiService == null) {  // Only do this once
                            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                                    new AndroidJsonFactory(), null)
                                    // options for running against local devappserver
                                    // - 10.0.2.2 is localhost's IP address in Android emulator
                                    // - turn off compression when running against local devappserver
                                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                                        @Override
                                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                            abstractGoogleClientRequest.setDisableGZipContent(true);
                                        }
                                    });
                            // end options for devappserver

                            myApiService = builder.build();
                        }

                        try {
                            return myApiService.sayHi().execute().getJoke();
                        } catch (IOException e) {
                            return e.getMessage();
                        }
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        assertTrue ("Jokes fetched", !result.isEmpty());
//         Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                    }
                }.execute();
            }
        });

    /* The testing thread will wait here until the UI thread releases it
     * above with the countDown() or 30 seconds passes and it times out.
     */
        //signal.await(30, TimeUnit.SECONDS);
        // The task is done, and now you can assert some things!

//        assertTrue("Jokes fetched",true );
    }
}

