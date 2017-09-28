package com.worksit.app.commons.masks;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by SKYNET-DEV01 on 30/08/2017.
 */

public class CepWatcher implements TextWatcher {
    private boolean isRunning = false;
    private boolean isDeleting = false;
    private final String mask;

    public CepWatcher(String mask) {
        this.mask = mask;
    }

    public static CepWatcher buildCEPMask() {
        return new CepWatcher("#####-###");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        isDeleting = count > after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isRunning || isDeleting) {
            return;
        }
        isRunning = true;

        int editableLength = editable.length();
        if (editableLength < mask.length()) {
            if (mask.charAt(editableLength) != '#') {
                editable.append(mask.charAt(editableLength));
            } else if (mask.charAt(editableLength-1) != '#') {
                editable.insert(editableLength-1, mask, editableLength-1, editableLength);
            }
        }

        isRunning = false;
    }
}