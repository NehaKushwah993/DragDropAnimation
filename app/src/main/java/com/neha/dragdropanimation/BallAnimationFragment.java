package com.neha.dragdropanimation;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Neha Kushwah on 6/8/15.
 */
public class BallAnimationFragment extends Fragment implements View.OnLongClickListener,
        View.OnDragListener {

    public static final String TAG = BallAnimationFragment.class.getName();
    private SeekBar seekBarCurrentPercantage;
    private TextView tvPercentageBalls;
    private DragContainer mDragContainer;
    private ImageView imageViewRed;
    private ImageView imageViewBlue;
    private View currentDraggingView;
    private int numberOfRedBalls = 50;
    private final int numberOfTotalBalls = 100;
    private TextView tvNumberOfBlueBalls;
    private TextView tvNumberOfRedBalls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_balls_view, container,
                false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViews(getView());
        setCurrentValues();
    }


    /**
     * set current values in textViews
     */
    public void setCurrentValues() {
        seekBarCurrentPercantage.setMax(numberOfTotalBalls);
        seekBarCurrentPercantage.setProgress((numberOfTotalBalls - numberOfRedBalls));
        tvPercentageBalls.setText(((((numberOfTotalBalls - numberOfRedBalls) * 100) / numberOfTotalBalls) + "%"));
        tvNumberOfBlueBalls.setText(new StringBuilder().append(((numberOfTotalBalls - numberOfRedBalls) * 100) /
                numberOfTotalBalls).append("%").toString());
        tvNumberOfRedBalls.setText(new StringBuilder().append(((numberOfRedBalls) * 100) / numberOfTotalBalls).append("%").toString());
    }

    /**
     * Assignment of variable to their respected views and related work
     *
     * @param rootView
     */
    private void setupViews(View rootView) {

        seekBarCurrentPercantage = rootView.findViewById(R.id.seekBarCurrentPercentage);
        imageViewRed = rootView.findViewById(R.id.imageViewRed);
        tvPercentageBalls = rootView.findViewById(R.id.tvPercentageBalls);
        tvNumberOfBlueBalls = rootView.findViewById(R.id.tvNumberOfBlueBalls);
        tvNumberOfRedBalls = rootView.findViewById(R.id.tvNumberOfRedBalls);
        imageViewBlue = rootView.findViewById(R.id.imageViewBlue);
        mDragContainer = (DragContainer) rootView;// (DragContainer) rootView.findViewById(R.id.rl_top_circle_view);

        // create circular image on imageView
        imageViewRed.setImageBitmap(BitmapHelper.getCircleBitmap((int) (getResources().getDimension
                        (R.dimen.ball_width)),
                getResources().getColor(R.color.red),
                getResources().getColor(R.color.dark_red), (int) (getResources().getDimension
                        (R.dimen.ball_boundary_width))));
        imageViewBlue.setImageBitmap(BitmapHelper.getCircleBitmap((int) (getResources().getDimension
                        (R.dimen.ball_width)),
                getResources().getColor(R.color.blue), getResources()
                        .getColor(R.color.dark_blue),  (int) (getResources().getDimension
                        (R.dimen.ball_boundary_width))));

        // to make userInteraction disabled
//        seekBarCurrentPercantage.setEnabled(false);

        // to enable dragGesture after longPress
        rootView.findViewById(R.id.imageViewBlue).setOnLongClickListener(this);
        rootView.findViewById(R.id.imageViewRed).setOnLongClickListener(this);

    }

    @Override
    public boolean onLongClick(View view) {

        // if dragging view is redball than container should be blue ball & stop long press when there is no ball left in red container
        // same for blueball
        if (view.getId() == R.id.imageViewRed && numberOfRedBalls > 0) {
            // set imageViewBlue as container for RedBalls
            imageViewBlue.setOnDragListener(this);
            imageViewRed.setOnDragListener(null);

            currentDraggingView = view;

            // enable drag gesture for Red Balls
            setDragListener(view);
            return true;
        } else if (view.getId() == R.id.imageViewBlue && (numberOfTotalBalls - numberOfRedBalls) > 0) {
            // set imageViewRed as container for BlueBall
            imageViewRed.setOnDragListener(this);
            imageViewBlue.setOnDragListener(null);

            currentDraggingView = view;
            // enable drag gesture for Blue Balls
            setDragListener(view);
            return true;
        } else return false;


    }

    private void setDragListener(View view) {

        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
        // to show actual object instead of shadow (while dragging)
        ClipData dragData = new ClipData((CharSequence) view.getTag(),
                new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

        // DragContainer has its own DragShadowBuilder
        mDragContainer.startDragChild(view, dragData, // the data to be dragged
                null, // no need to use local data
                0 // flags (not currently used, set to 0)
        );
    }

    @Override
    public boolean onDrag(View layoutView, DragEvent dragevent) {

        int action = dragevent.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                Log.d(TAG, "Drag event started");

                break;
            case DragEvent.ACTION_DRAG_LOCATION:

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "Drag event entered ");

                //for scaling by 30%
                scaleViewWithAnimation(layoutView, 0.3f);

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "Drag event exited ");

                //for scaling back to normal state
                scaleViewWithAnimation(layoutView, 0f);

                break;
            case DragEvent.ACTION_DROP:
                Log.d(TAG, "Dropped");

                //for scaling back to normal state
                scaleViewWithAnimation(layoutView, 0f);
                manageBallValues();

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                Log.d(TAG, "Drag ended");
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * increment or decrement values of red nad blue balls
     */
    private void manageBallValues() {

        if (currentDraggingView == imageViewRed) {
            if (numberOfRedBalls > 0)
                numberOfRedBalls--;
        } else if (currentDraggingView == imageViewBlue) {
            if (numberOfRedBalls < 100)
                numberOfRedBalls++;
        }

        setCurrentValues();
    }

    /**
     * scaling animation on view
     *
     * @param view
     * @param scaleBy
     */
    public void scaleViewWithAnimation(View view, float scaleBy) {
        view.animate().scaleX(1.0f + scaleBy).scaleY(1.0f + scaleBy);
    }

}
