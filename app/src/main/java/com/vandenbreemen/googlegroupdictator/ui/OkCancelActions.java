package com.vandenbreemen.googlegroupdictator.ui;

import android.view.ViewGroup;

import com.vandenbreemen.googlegroupdictator.R;

/**
 * Yes/no actions
 * <br/>Created by kevin on 06/01/18.
 */
public class OkCancelActions {

    /**
     * On yes/ok action
     */
    private Runnable onOK;

    /**
     * On no/cancel
     */
    private Runnable onCancel;

    public OkCancelActions(Runnable onOK, Runnable onCancel) {
        this.onOK = onOK;
        this.onCancel = onCancel;
    }

    /**
     * Assign the actions to the OK and Cancel buttons in the group
     * @param group
     */
    public void assign(ViewGroup group){
        group.findViewById(R.id.ok).setOnClickListener(v -> onOK.run());
        group.findViewById(R.id.cancel).setOnClickListener(v -> onCancel.run());
    }
}
