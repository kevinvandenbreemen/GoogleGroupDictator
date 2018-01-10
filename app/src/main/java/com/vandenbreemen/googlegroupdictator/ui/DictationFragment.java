package com.vandenbreemen.googlegroupdictator.ui;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.vandenbreemen.googlegroupdictator.R;
import com.vandenbreemen.googlegroupdictator.mvp.controller.DictationController;
import com.vandenbreemen.googlegroupdictator.mvp.view.DictationView;
import com.vandenbreemen.googlegroupdictator.util.msg.ApplicationError;
import com.vandenbreemen.googlegroupdictator.util.msg.RuntimeError;

/**
 *
 * <br/>Created by kevin on 07/01/18.
 */
public class DictationFragment extends Fragment implements DictationView{


    /**
     * Callback for when dictation is dismissed/closed
     */
    public static interface OnDictationClosedListener{

        /**
         * Dictation has been closed
         */
        public void onCloseDictation();
    }

    private DictationController controller;

    /**
     * Seeker bar
     */
    private SeekBar seekBar;

    /**
     * Listener for when the dictation window is closed
     */
    private OnDictationClosedListener listener;

    public DictationFragment setController(DictationController controller) {
        this.controller = controller;
        return this;
    }

    @Override
    public void setMax(int maxUtterances) {
        seekBar.setMax(maxUtterances);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ViewGroup ui = (ViewGroup) inflater.inflate(R.layout.layout_player, container, false);
        ui.findViewById(R.id.closeButton).setOnClickListener(v -> controller.onClose());
        ui.findViewById(R.id.pauseButton).setOnClickListener(v -> controller.onPause());
        ui.findViewById(R.id.playButton).setOnClickListener(v -> controller.onPlay());

        this.seekBar = ui.findViewById(R.id.seekBar);

        controller.setView(this);
        controller.start();

        return ui;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(!(context instanceof OnDictationClosedListener)){
            throw new RuntimeError("Activity using this fragment must implement "+OnDictationClosedListener.class.getSimpleName());
        }

        this.listener = (OnDictationClosedListener) context;

    }

    @Override
    public void showError(ApplicationError error) {
        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updatePosition(int position) {
        seekBar.setProgress(position);
    }

    @Override
    public void onDone() {
        this.controller = null;
        this.listener.onCloseDictation();
    }
}
