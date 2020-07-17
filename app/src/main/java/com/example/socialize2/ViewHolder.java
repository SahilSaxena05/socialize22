package com.example.socialize2;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewHolder extends RecyclerView.ViewHolder {
    SimpleExoPlayer exoPlayer;
    PlayerView playerView;
    ImageButton likebutton,dislikebutton;
    TextView likesdisplay,dislikesdisplay;
    int likescount,dislikescount;
    DatabaseReference likesref,dislikesref;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());

            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                mClickListener.onItemLongClick(view,getAdapterPosition());
                return false;

            }
        });
    }

    public void setLikesbuttonStatus(final String postkey){
        likebutton = itemView.findViewById(R.id.like_btn);

        likesdisplay = itemView.findViewById(R.id.likes_textview);

        likesref = FirebaseDatabase.getInstance().getReference("likes");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String likes = "likes";

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
           if(dataSnapshot.child(postkey).hasChild(userId)){
               likescount = (int)dataSnapshot.child(postkey).getChildrenCount();
               likebutton.setImageResource(R.drawable.ic_baseline_thumb_up_24);
               likesdisplay.setText(Integer.toString(likescount)+likes);
           }
           else{
               likescount = (int)dataSnapshot.child(postkey).getChildrenCount();
               likebutton.setImageResource(R.drawable.ic_baseline_thumb_up_alt_24);
               likesdisplay.setText(Integer.toString(likescount)+likes);
           }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public   void setDislikesbuttonStatus(final String prekey){

        dislikebutton = itemView.findViewById(R.id.dislike_btn);
        dislikesdisplay = itemView.findViewById(R.id.dislikes_textview);
        dislikesref = FirebaseDatabase.getInstance().getReference("dislikes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String dislikes = "dislikes";

        dislikesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(prekey).hasChild(userId)){
                    dislikescount = (int)snapshot.child(prekey).getChildrenCount();
                    dislikebutton.setImageResource(R.drawable.ic_baseline_thumb_down_24);
                    dislikesdisplay.setText(Integer.toString(dislikescount)+dislikes);
                }
                else{
                    dislikescount = (int)snapshot.child(prekey).getChildrenCount();
                    dislikebutton.setImageResource(R.drawable.ic_baseline_thumb_down_alt_24);
                    dislikesdisplay.setText(Integer.toString(dislikescount)+dislikes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void  setExoplayer(Application application ,String name,String Videourl) {

        TextView textView= itemView.findViewById(R.id.tv_item_name);
        playerView = itemView.findViewById(R.id.exoplayer_item);

        textView.setText(name);

        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(application).build();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer= (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(application);
            Uri video = Uri.parse(Videourl);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(video,dataSourceFactory,extractorsFactory,null,null);
                    playerView.setPlayer(exoPlayer);
                    exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);

        }catch (Exception e){
            Log.e("ViewHolder","exoplayer error"+e.toString());
        }
    }
    private ViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view , int position);
        void onItemLongClick(View view,int position);

    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClickListener = clickListener;
    }


}
