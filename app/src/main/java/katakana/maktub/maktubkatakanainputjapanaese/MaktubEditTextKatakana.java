package katakana.maktub.maktubkatakanainputjapanaese;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Collator;
import java.util.Locale;

/**
 * Created by ADMIN on 12/18/16.
 */

public class MaktubEditTextKatakana extends EditText {
    private int mIndex = 0;
    private boolean mIsInputRomajiFirst = false;
    private int mCountInput = 0;
    private boolean mIsUsingRomajiKeyBoard = true;
    private boolean isFirstRun = true;

    public MaktubEditTextKatakana(Context context) {
        super(context);
        initAction(context);
    }

    public MaktubEditTextKatakana(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAction(context);
    }

    public MaktubEditTextKatakana(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAction(context);
    }

    private void initAction(final Context context) {
        setLines(1);
        setSingleLine(true);
        setImeOptions(EditorInfo.IME_ACTION_DONE);
        isFirstRun = true;
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                if (charSequence.length() > 1) {
                    if (mIsUsingRomajiKeyBoard) {
                        if (!isInputKatagana(charSequence)) {
                            if (mIsInputRomajiFirst) {
                                Collator usCollator = Collator.getInstance(Locale.JAPANESE);
                                usCollator.setStrength(Collator.PRIMARY);
                                if (spanned.length() > 0) {
                                    if (usCollator.compare(spanned.toString().substring(mIndex, mIndex + 1), String.valueOf(charSequence.charAt(0))) != 0) {
                                        checkListResult(charSequence);
                                    }
                                }
                            } else {
                                checkListResult(charSequence);
                            }
                        } else {
                            resetAllValues();
                        }
                    } else {
                        Collator usCollator = Collator.getInstance(Locale.JAPANESE);
                        usCollator.setStrength(Collator.PRIMARY);
                        if (spanned.length() > 0) {
                            if (usCollator.compare(spanned.toString().substring(mIndex, mIndex + 1), String.valueOf(charSequence.charAt(0))) != 0) {
                                checkListResult(charSequence);
                            }
                        }
                    }
                } else {
                    if (charSequence.length() == 1) {
                        /**
                         * Validate input.
                         * Accept three values : BASIC_LATIN | HIRAGANA | HALFWIDTH_AND_FULLWIDTH_FORMS | KATAKANA
                         * If fail : Remove all values fail.
                         */
                        if (Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.BASIC_LATIN
                                || Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HIRAGANA
                                || Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                                || Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.KATAKANA) {
                            mIndex = spanned.length() - 1 < 0 ? 0 : spanned.length() - 1;
                            if (mIndex < 0)
                                mIndex = 0;
                            if (Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.KATAKANA) {
                                resetAllValues();
                            } else {
                                if (mCountInput == 0) {
                                    //Phải thêm 1 biến check HALFWIDTH_AND_FULLWIDTH_FORMS
                                    // Bỏi vì bàn phím nhập tiếng nhật custome là dạng này.
                                    if (Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.BASIC_LATIN || Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                                        mIsInputRomajiFirst = Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.BASIC_LATIN || Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
                                        if (!mIsInputRomajiFirst) {
                                            if (Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HIRAGANA) {
                                                removeAllHiragana();
                                            }
                                        }
                                    }
                                    if (Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.HIRAGANA) {
                                        if (isFirstRun) {
                                            isFirstRun = false;
                                            mIsUsingRomajiKeyBoard = Character.UnicodeBlock.of(charSequence.charAt(0)) == Character.UnicodeBlock.BASIC_LATIN;
                                        }
                                    }
                                }
                            }
                        } else {
                            removeAllHiragana();
                        }
                    }
                    if (charSequence.length() == 0) {
                        resetAllValues();
                    }
                }
                /**
                 * TODO : Kiểm tra việc người dùng bắt đầu nhập kí tự mới chưa.
                 */
                if (charSequence.length() > 0)
                    mCountInput++;
                return null;
            }
        };
        this.setFilters(new InputFilter[]{inputFilter});
        /**
         * Remove all chracter not is Katagana character.
         */
        this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    removeAllHiragana();
                }
            }
        });
        this.setOnEditorActionListener(new OnEditorActionListener() {
                                           @Override
                                           public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                                               if (event == null) {
                                                   if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                       removeAllHiragana();
                                                   }
                                               }
                                               return true;
                                           }
                                       }
        );
    }

    private void resetAllValues() {
        mIsInputRomajiFirst = false;
        mCountInput = 0;
    }

    private void checkListResult(CharSequence charSequence) {
        boolean values = true;
        String result = this.getText().toString();
        for (int i = 0; i < charSequence.length(); i++) {
            if (Character.UnicodeBlock.of(charSequence.charAt(i)) != Character.UnicodeBlock.KATAKANA)
                values = false;
        }
        if (!values) {
            result = result.replace(charSequence.toString(), "");
        }
        setText(result);
        removeAllHiragana();
        resetAllValues();
        Log.e("Clear", "Please input katagana");
    }

    private boolean isInputKatagana(CharSequence charSequence) {
        boolean values = true;
        for (int i = 0; i < charSequence.length(); i++) {
            if (Character.UnicodeBlock.of(charSequence.charAt(i)) != Character.UnicodeBlock.KATAKANA)
                values = false;
        }
        return values;
    }

    public void removeAllHiragana() {
        String result = this.getText().toString();
        String needForSpeed = "";
        for (int j = 0; j < result.length(); j++) {
            if (Character.UnicodeBlock.of(result.charAt(j)) == Character.UnicodeBlock.KATAKANA) {
                needForSpeed += result.charAt(j);
            }
        }
        this.setText(needForSpeed);
        this.setSelection(this.getText().length());
        Log.e("Clear", "Please input katagana");
    }
}
