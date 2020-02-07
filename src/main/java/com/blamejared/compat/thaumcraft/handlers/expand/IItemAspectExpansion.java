package com.blamejared.compat.thaumcraft.handlers.expand;

import com.blamejared.ModTweaker;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspect;
import com.blamejared.compat.thaumcraft.handlers.aspects.CTAspectStack;
import com.blamejared.mtlib.helpers.*;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.*;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.*;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectHelper;

import java.util.*;

@ZenExpansion("crafttweaker.item.IItemStack")
@ModOnly("thaumcraft")
@ZenRegister
public class IItemAspectExpansion {
    
    @ZenMethod
    public static void setAspects(IItemStack stack, CTAspectStack... aspects) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction("Aspects") {
            @Override
            public void apply() {
                AspectList list = new AspectList();
                for(CTAspectStack aspect : aspects) {
                    list.add(aspect.getInternal().getInternal(), aspect.getAmount());
                }
                ThaumcraftApi.registerObjectTag(InputHelper.toStack(stack), list);
            }
            
            @Override
            public String describe() {
                return "Setting aspects on item: " + LogHelper.getStackDescription(stack) + " to: " + getAspects();
            }
            
            private String getAspects() {
                StringBuilder builder = new StringBuilder();
                for(CTAspectStack aspect : aspects) {
                    builder.append(aspect.getInternal().getInternal().getName()).append(", ");
                }
                return builder.reverse().deleteCharAt(0).deleteCharAt(0).reverse().toString();
            }
            
        });
        
    }
	
	@ZenMethod
	public static boolean hasAspects(IItemStack stack)
	{
		final AspectList check = AspectHelper.getObjectAspects(InputHelper.toStack(stack));
		
		if (check == null)
			return false;
		
		return (check.getAspects().length > 0);
	}
    
	@ZenMethod
	public static CTAspectStack[] getAspects(IItemStack stack)
	{
		final AspectList list = new AspectList(InputHelper.toStack(stack));
		final Aspect[] tcArray = list.getAspects();
		final int len = tcArray.length;
		final CTAspectStack[] result = new CTAspectStack[len];
		
		for (int i = 0; i < len; i++)
		{
			final Aspect aspect = tcArray[i];
			result[i] = new CTAspectStack(new CTAspect(aspect), list.getAmount(aspect));
		}
		
		return result;
	}
    
    @ZenMethod
    public static void removeAspects(IItemStack stack, CTAspectStack... aspects) {
        ModTweaker.LATE_REMOVALS.add(new BaseAction("Aspects") {
            
            @Override
            public void apply() {
                AspectList list = new AspectList(InputHelper.toStack(stack));
                for(CTAspectStack aspect : aspects) {
                    list.remove(aspect.getInternal().getInternal());
                }
                ThaumcraftApi.registerObjectTag(InputHelper.toStack(stack), list);
            }
    
            @Override
            public String describe() {
                return "Removing aspects on item: " + LogHelper.getStackDescription(stack) + "," + getAspects();
            }
            
            private String getAspects() {
                StringBuilder builder = new StringBuilder();
                for(CTAspectStack aspect : aspects) {
                    builder.append(aspect.getInternal().getInternal().getName()).append(", ");
                }
                return builder.reverse().deleteCharAt(0).deleteCharAt(0).reverse().toString();
            }
        });
        
    }
}
