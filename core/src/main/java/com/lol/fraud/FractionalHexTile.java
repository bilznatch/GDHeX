package com.lol.fraud;

import java.util.ArrayList;

public class FractionalHexTile {
    public final double q;
    public final double r;
    public final double s;

    public FractionalHexTile(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (Math.round(q + r + s) != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }

    public HexTile hexRound() {
        int qi = (int)(Math.round(q));
        int ri = (int)(Math.round(r));
        int si = (int)(Math.round(s));
        double q_diff = Math.abs(qi - q);
        double r_diff = Math.abs(ri - r);
        double s_diff = Math.abs(si - s);
        if (q_diff > r_diff && q_diff > s_diff)
        {
            qi = -ri - si;
        }
        else
        if (r_diff > s_diff)
        {
            ri = -qi - si;
        }
        else
        {
            si = -qi - ri;
        }
        return new HexTile(qi, ri, si);
    }


    public FractionalHexTile hexLerp(FractionalHexTile b, double t) {
        return new FractionalHexTile(q * (1.0 - t) + b.q * t, r * (1.0 - t) + b.r * t, s * (1.0 - t) + b.s * t);
    }


    static public ArrayList<HexTile> hexLinedraw(HexTile a, HexTile b) {
        int N = a.distance(b);
        FractionalHexTile a_nudge = new FractionalHexTile(a.q + 1e-06, a.r + 1e-06, a.s - 2e-06);
        FractionalHexTile b_nudge = new FractionalHexTile(b.q + 1e-06, b.r + 1e-06, b.s - 2e-06);
        ArrayList<HexTile> results = new ArrayList<HexTile>();
        double step = 1.0 / Math.max(N, 1);
        for (int i = 0; i <= N; i++) {
            results.add(a_nudge.hexLerp(b_nudge, step * i).hexRound());
        }
        return results;
    }

}
