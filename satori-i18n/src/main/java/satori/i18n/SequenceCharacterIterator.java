package satori.i18n;

import java.text.CharacterIterator;

/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation Zend
 * Technologies
 * 
 * 
 * /** A <code>CharSequence</code> based implementation of
 * <code>CharacterIterator</code>.
 */
public class SequenceCharacterIterator implements CharacterIterator {

    private int fIndex = -1;
    private final CharSequence fSequence;
    private final int fFirst;
    private final int fLast;

    /**
     * Creates an iterator for the entire sequence.
     * 
     * @param sequence
     *            the sequence backing this iterator
     */
    public SequenceCharacterIterator(CharSequence sequence) {
        this(sequence, 0);
    }

    /**
     * Creates an iterator.
     * 
     * @param sequence
     *            the sequence backing this iterator
     * @param first
     *            the first character to consider
     * @throws IllegalArgumentException
     *             if the indices are out of bounds
     */
    public SequenceCharacterIterator(CharSequence sequence, int first) throws IllegalArgumentException {
        this(sequence, first, sequence.length());
    }

    /**
     * Creates an iterator.
     * 
     * @param sequence
     *            the sequence backing this iterator
     * @param first
     *            the first character to consider
     * @param last
     *            the last character index to consider
     * @throws IllegalArgumentException
     *             if the indices are out of bounds
     */
    public SequenceCharacterIterator(CharSequence sequence, int first, int last) throws IllegalArgumentException {
        if (sequence == null)
            throw new NullPointerException();
        if (first < 0 || first > last)
            throw new IllegalArgumentException();
        if (last > sequence.length())
            throw new IllegalArgumentException();
        fSequence = sequence;
        fFirst = first;
        fLast = last;
        fIndex = first;
    }

    /*
     * @see java.text.CharacterIterator#first()
     */
    public char first() {
        return setIndex(getBeginIndex());
    }

    /*
     * @see java.text.CharacterIterator#last()
     */
    public char last() {
        if (fFirst == fLast)
            return setIndex(getEndIndex());
        else
            return setIndex(getEndIndex() - 1);
    }

    /*
     * @see java.text.CharacterIterator#current()
     */
    public char current() {
        if (fIndex >= fFirst && fIndex < fLast)
            return fSequence.charAt(fIndex);
        else
            return DONE;
    }

    /*
     * @see java.text.CharacterIterator#next()
     */
    public char next() {
        return setIndex(Math.min(fIndex + 1, getEndIndex()));
    }

    /*
     * @see java.text.CharacterIterator#previous()
     */
    public char previous() {
        if (fIndex > getBeginIndex()) {
            return setIndex(fIndex - 1);
        } else {
            return DONE;
        }
    }

    /*
     * @see java.text.CharacterIterator#setIndex(int)
     */
    public char setIndex(int position) {
        if (position >= getBeginIndex() && position <= getEndIndex())
            fIndex = position;
        else
            throw new IllegalArgumentException();

        return current();
    }

    /*
     * @see java.text.CharacterIterator#getBeginIndex()
     */
    public int getBeginIndex() {
        return fFirst;
    }

    /*
     * @see java.text.CharacterIterator#getEndIndex()
     */
    public int getEndIndex() {
        return fLast;
    }

    /*
     * @see java.text.CharacterIterator#getIndex()
     */
    public int getIndex() {
        return fIndex;
    }

    /*
     * @see java.text.CharacterIterator#clone()
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
