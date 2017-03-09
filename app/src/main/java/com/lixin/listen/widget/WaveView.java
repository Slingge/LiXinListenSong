/**   
 * Copyright © 2014 All rights reserved.
 * 
 * @Title: WaveView.java 
 * @Prject: WaveAnimation
 * @Package: com.example.waveanimation 
 * @Description: TODO
 * @author: raot  719055805@qq.com
 * @date: 2014年9月16日 下午4:57:26 
 * @version: V1.0   
 */
package com.lixin.listen.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @ClassName: WaveView
 * @Description: TODO
 * @author: raot 719055805@qq.com
 * @date: 2014年9月16日 下午4:57:26
 */
public class WaveView extends View implements Runnable {

	private Paint paint1 = new Paint();
	private Paint paint2 = new Paint();
	private Paint paint3 = new Paint();
	private boolean isRun = false;
	private int angle = 0;

	public WaveView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		int height = getHeight();
		int width = getWidth();
		paint1.setColor(Color.rgb(205, 243, 246));
		paint2.setAlpha(200);
		paint2.setColor(Color.rgb(150, 206, 231));
		paint3.setAlpha(150);
		paint3.setColor(Color.rgb(89, 186, 231));
		double lineX = 0;
		double lineY1 = 0;
		double lineY2 = 0;
		double lineY3 = 0;
		for (int i = 0; i < width; i++) {
			lineX = i;
			if (isRun) {
				lineY1 = Math.sin((i + angle) * Math.PI / 180);
				lineY2 = 10 * Math.sin((i + angle) * Math.PI / 180) + 20;
				lineY3 = 20 * Math.sin((i + angle) * Math.PI / 180) + 50;
			} else {
				lineY1 = 0;
				lineY2 = 20;
				lineY3 = 50;
			}
			canvas.drawLine((int) lineX, (int) (lineY1 + height / 1.5),
					(int) lineX + 1, (int) (lineY2 + height / 1.5), paint1);
			canvas.drawLine((int) lineX, (int) (lineY2 + height / 1.5),
					(int) lineX + 1, (int) (lineY3 + height / 1.5), paint2);
			canvas.drawLine((int) lineX, (int) (lineY3 + height / 1.5),
					(int) lineX + 1, height, paint3);
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			angle++;
			if (angle == 360) {
				angle = 0;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void start() {
		isRun = true;
		new Thread(this).start();
	}

	public void stop() {
		isRun = false;
		angle = 0;
	}

}
