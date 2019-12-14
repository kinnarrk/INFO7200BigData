/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kinnar.bigdataproject.recommendation_sys;


import org.apache.hadoop.io.WritableComparator;

public class GroupComparator extends WritableComparator{

	protected GroupComparator() {
		super(CompositeKey.class, true);
	}

	@Override
	public int compare(Object a, Object b) {
		
		CompositeKey ckw1 = (CompositeKey)a;
		CompositeKey ckw2 = (CompositeKey)b;
		
		return ckw1.getSrcDest().compareTo(ckw2.getSrcDest());
	}
}
	
