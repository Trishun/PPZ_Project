package pl.locationbasedgame;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LobbyRootFragment extends Fragment {

    private String TAG = "LOBBY_ROOT";
    private Unbinder unbinder;
    private View rootView;
    private Integer team;
    private String myName;

    public LobbyRootFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lobby_root, container, false);
        try {
            setLobbyEnter(getArguments().getString("enter_code"), rootView);
        } catch (NullPointerException e) {
            Log.i(TAG, String.valueOf(e));
        }
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    void setAdapter(JSONObject message) {
        ArrayList<ListViewPlayerItem> listContact = processPlayersList(message);
        ListView lv = (ListView) rootView.findViewById(R.id.lv_player_list);
        lv.setAdapter(new ListviewPlayerAdapter(getActivity(), listContact));
    }

    private void setLobbyEnter(String text, View view) {
        TextView textView = (TextView) view.findViewById(R.id.lobbyId);
        textView.setText(text);
    }

    Integer getTeam() {
        return team;
    }

    void setMyName(String myName) {
        this.myName = myName;
    }

    private ArrayList<ListViewPlayerItem> processPlayersList(JSONObject message) {
        ArrayList<ListViewPlayerItem> playerList = new ArrayList<>();
        try {
            JSONArray lobbyStructure = message.getJSONArray("llist");
            JSONArray teamEscape = lobbyStructure.getJSONArray(0);
            JSONArray teamPursuit = lobbyStructure.getJSONArray(1);
            JSONArray uncategorized = lobbyStructure.getJSONArray(2);
            String playerName;

            for (int i = 0; i < teamEscape.length(); i++) {
                playerName = teamEscape.get(i).toString();
                playerList.add(new ListViewPlayerItem(playerName, getString(R.string.escape_team)));
                if (playerName.equalsIgnoreCase(myName))
                    team = 0;
            }

            for (int i = 0; i < teamPursuit.length(); i++) {
                playerName = teamPursuit.get(i).toString();
                playerList.add(new ListViewPlayerItem(playerName, getString(R.string.pursuit_team)));
                if (playerName.equalsIgnoreCase(myName))
                    team = 1;
            }

            for (int i = 0; i < uncategorized.length(); i++) {
                playerList.add(new ListViewPlayerItem(uncategorized.get(i).toString(), getString(R.string.uncategorized)));
            }

        } catch (JSONException e) {
            Log.i(TAG, String.valueOf(e));
        }
        return playerList;
    }

    @OnClick(R.id.btn_join_escape)
    void joinEscape() {
        ((LobbyActivity) getActivity()).getService().sendComplexMessage("tjoin", "team", "0");
        team = 0;
    }

    @OnClick(R.id.btn_join_pursuit)
    void joinPursuit() {
        ((LobbyActivity) getActivity()).getService().sendComplexMessage("tjoin", "team", "1");
        team = 1;
    }

    @OnClick(R.id.btn_ready_game_begin)
    void beginGame() {
        ((LobbyActivity) getActivity()).getService().sendSimpleMessage("gbegin");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ((LobbyActivity) getActivity()).setContentView(R.layout.activity_lobby);
    }

}

class ListViewPlayerItem {
    private String name;
    private String team;

    ListViewPlayerItem(String name, String team) {
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}

class ListviewPlayerAdapter extends BaseAdapter {
    private static ArrayList<ListViewPlayerItem> listContact;

    private LayoutInflater mInflater;

    ListviewPlayerAdapter(Context photosFragment, ArrayList<ListViewPlayerItem> results) {
        listContact = results;
        mInflater = LayoutInflater.from(photosFragment);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listContact.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return listContact.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.tv_player_name);
            holder.txtteam = (TextView) convertView.findViewById(R.id.tv_player_team);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtname.setText(listContact.get(position).getName());
        holder.txtteam.setText(listContact.get(position).getTeam());

        return convertView;
    }

    static class ViewHolder {
        TextView txtname, txtteam;
    }
}