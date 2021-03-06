package cloudapplications.citycheck.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import cloudapplications.citycheck.APIService.NetworkManager;
import cloudapplications.citycheck.APIService.NetworkResponseListener;
import cloudapplications.citycheck.Models.Game;
import cloudapplications.citycheck.Models.Team;
import cloudapplications.citycheck.R;
import cloudapplications.citycheck.TeamsAdapter;

public class GameCodeActivity extends AppCompatActivity {

    private String currentGameCode;
    private String currentGameTime;

    private long millisStarted;

    private Boolean gotTeams;
    private NetworkManager service;
    private ArrayList<Team> prevTeams = new ArrayList<>();
    private ArrayList<Team> teamsList = new ArrayList<>();

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getTeams();
        }
    };

    private TextView codeTextView;
    private TextView timeTextView;
    private ListView teamsListView;
    private TextView teamsTextView;

    private Button startGameButton;

    //callbacks
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_code);

        service = NetworkManager.getInstance();

        startGameButton = findViewById(R.id.button_start_game);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.button);
        codeTextView = findViewById(R.id.text_view_code);
        timeTextView = findViewById(R.id.text_view_time);
        teamsTextView = findViewById(R.id.text_view_teams);
        teamsListView = findViewById(R.id.teams_list_view);

        currentGameCode = Objects.requireNonNull(getIntent().getExtras()).getString("gameCode");
        currentGameTime = getIntent().getExtras().getString("gameTime");

        codeTextView.setText(currentGameCode);
        timeTextView.setText(currentGameTime + " hours");

        // If the game creator came to this view then he has the right to start the game
        if (!getIntent().getExtras().getBoolean("gameCreator"))
            startGameButton.setVisibility(View.GONE);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                startGameButton.setEnabled(false);
                creatorStartGame();
            }
        });

        gotTeams = false;
        getTeams();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
    }

    //private methodes
    private void getTeams() {
        handler.postDelayed(runnable, 3000);
        service.getCurrentGame(Integer.parseInt(currentGameCode), new NetworkResponseListener<Game>() {
            @Override
            public void onResponseReceived(Game game) {
                if (game.getHasStarted()) {
                    millisStarted = game.getMillisStarted();
                    startGame();
                } else {
                    if (game.getTeams().size() != prevTeams.size()) {
                        prevTeams = game.getTeams();
                        if (gotTeams) {
                            Team team = game.getTeams().get(game.getTeams().size() - 1);
                            team.setPunten(-1);
                            teamsList.add(team);
                        } else {
                            teamsTextView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < game.getTeams().size(); i++) {
                                Team team = game.getTeams().get(i);
                                team.setPunten(-1);
                                teamsList.add(team);
                            }
                        }
                        teamsListView.setAdapter(new TeamsAdapter(GameCodeActivity.this.getBaseContext(), teamsList));
                    } else {
                        gotTeams = true;
                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(GameCodeActivity.this, "Error while trying to get the teams", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGame() {
        Intent i = new Intent(GameCodeActivity.this, GameActivity.class);
        i.putExtra("gameTime", currentGameTime);
        i.putExtra("gameCode", currentGameCode);
        i.putExtra("teamNaam", Objects.requireNonNull(getIntent().getExtras()).getString("teamNaam"));
        i.putExtra("millisStarted", String.valueOf(millisStarted));

        if (getIntent().getExtras().getBoolean("gameCreator"))
            i.putExtra("gameCreator", true);
        else
            i.putExtra("gameCreator", false);

        startActivity(i);
    }

    private void creatorStartGame() {
        millisStarted = System.currentTimeMillis();
        service.startGame(Integer.parseInt(currentGameCode), millisStarted, new NetworkResponseListener<Double>() {
            @Override
            public void onResponseReceived(Double aDouble) {
                startGame();
            }

            @Override
            public void onError() {
                Toast.makeText(GameCodeActivity.this, "Error while trying to start the game", Toast.LENGTH_SHORT).show();
                startGameButton.setEnabled(true);
            }
        });
    }

}
