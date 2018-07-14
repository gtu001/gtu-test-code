package com.example.englishtester.common;

import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableString;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by gtu001 on 2018/7/14.
 */

public class TxtCoordinateFetcher {

    Rect parentTextViewRect;
    TextView parentTextView;
    WindowManager mWindowManager;

    public TxtCoordinateFetcher(final TextView parentTextView, Object spanObject, WindowManager mWindowManager) {
        this.parentTextView = parentTextView;
        this.mWindowManager = mWindowManager;

        // Initialize global value
        this.parentTextViewRect = new Rect();


// Initialize values for the computing of clickedText position
        SpannableString completeText = (SpannableString) (parentTextView).getText();
        Layout textViewLayout = parentTextView.getLayout();

        double startOffsetOfClickedText = completeText.getSpanStart(spanObject);
        double endOffsetOfClickedText = completeText.getSpanEnd(spanObject);
        double startXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) startOffsetOfClickedText);
        double endXCoordinatesOfClickedText = textViewLayout.getPrimaryHorizontal((int) endOffsetOfClickedText);


// Get the rectangle of the clicked text
        int currentLineStartOffset = textViewLayout.getLineForOffset((int) startOffsetOfClickedText);
        int currentLineEndOffset = textViewLayout.getLineForOffset((int) endOffsetOfClickedText);
        boolean keywordIsInMultiLine = currentLineStartOffset != currentLineEndOffset;
        textViewLayout.getLineBounds(currentLineStartOffset, this.parentTextViewRect);


// Update the rectangle position to his real position on screen
        int[] parentTextViewLocation = {0, 0};
        parentTextView.getLocationOnScreen(parentTextViewLocation);

        double parentTextViewTopAndBottomOffset = (
                parentTextViewLocation[1] -
                        parentTextView.getScrollY() +
                        this.parentTextView.getCompoundPaddingTop()
        );
        this.parentTextViewRect.top += parentTextViewTopAndBottomOffset;
        this.parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;

// In the case of multi line text, we have to choose what rectangle take
        if (keywordIsInMultiLine) {

            int screenHeight = this.mWindowManager.getDefaultDisplay().getHeight();
            int dyTop = this.parentTextViewRect.top;
            int dyBottom = screenHeight - this.parentTextViewRect.bottom;
            boolean onTop = dyTop > dyBottom;

            if (onTop) {
                endXCoordinatesOfClickedText = textViewLayout.getLineRight(currentLineStartOffset);
            } else {
                this.parentTextViewRect = new Rect();
                textViewLayout.getLineBounds(currentLineEndOffset, this.parentTextViewRect);
                this.parentTextViewRect.top += parentTextViewTopAndBottomOffset;
                this.parentTextViewRect.bottom += parentTextViewTopAndBottomOffset;
                startXCoordinatesOfClickedText = textViewLayout.getLineLeft(currentLineEndOffset);
            }

        }

        this.parentTextViewRect.left += (
                parentTextViewLocation[0] +
                        startXCoordinatesOfClickedText +
                        this.parentTextView.getCompoundPaddingLeft() -
                        parentTextView.getScrollX()
        );
        this.parentTextViewRect.right = (int) (
                this.parentTextViewRect.left +
                        endXCoordinatesOfClickedText -
                        startXCoordinatesOfClickedText
        );
    }

    public Rect getCoordinate() {
        return parentTextViewRect;
    }
}
